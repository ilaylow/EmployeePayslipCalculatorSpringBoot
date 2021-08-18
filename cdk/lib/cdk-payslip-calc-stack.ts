import * as cdk from '@aws-cdk/core';
import { CdkPipeline, SimpleSynthAction } from "@aws-cdk/pipelines";
import * as ec2 from "@aws-cdk/aws-ec2";
import * as ecs from "@aws-cdk/aws-ecs";
import * as ecr from "@aws-cdk/aws-ecr";
import * as iam from "@aws-cdk/aws-iam";
import * as codebuild from '@aws-cdk/aws-codebuild';

import * as codepipeline from '@aws-cdk/aws-codepipeline';
import * as codepipeline_actions from '@aws-cdk/aws-codepipeline-actions';
import { SecretValue } from '@aws-cdk/core';
import { LocalDeploymentStage } from './local-deploy';

export class CdkPayslipCalcStack extends cdk.Stack {
  constructor(scope: cdk.Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);
    
    // The code that defines your stack goes here
    const sourceArtifact = new codepipeline.Artifact();
    const cloudAssemblyArtifact = new codepipeline.Artifact();

    const pipeline = new CdkPipeline(this, "Pipeline", {
      pipelineName: "Payslip-Pipeline",
      cloudAssemblyArtifact,
      
      sourceAction: new codepipeline_actions.GitHubSourceAction({
        actionName: "DownloadSource",
        output: sourceArtifact,
        oauthToken: SecretValue.secretsManager("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"),
        trigger: codepipeline_actions.GitHubTrigger.POLL,
        owner: "ilaylow",
        repo: "EmployeePayslipCalculatorSpringBoot",
      }),

      synthAction: SimpleSynthAction.standardNpmSynth({
        sourceArtifact,
        cloudAssemblyArtifact,
      })
    })

    // Build and Publish application artifacts
    const repository = new ecr.Repository(this, 'Repository', {
      repositoryName: 'cdk-cicd/payslip',
    });

    const buildRole = new iam.Role(this, 'DockerBuildRole', {
      assumedBy: new iam.ServicePrincipal('codebuild.amazonaws.com'),
    });
    repository.grantPullPush(buildRole);

    const buildStage = pipeline.addStage('AppBuild')
    buildStage.addActions(new codepipeline_actions.CodeBuildAction({
      actionName: 'DockerBuild',
      input: sourceArtifact,
      project: new codebuild.Project(this, 'DockerBuild', {
        role: buildRole,
        environment: {
          privileged: true,
        },
        buildSpec: this.getDockerBuildSpec(repository.repositoryUri),
      }),
    }));

    // Deploy - Local
    const localStage = new LocalDeploymentStage(this, 'AppDeployLocal');
    pipeline.addApplicationStage(localStage);

  }

  getDockerBuildSpec(repositoryUri: string): codebuild.BuildSpec {
    return codebuild.BuildSpec.fromObject({
      version: '0.2',
      phases: {
        pre_build: {
          commands: [
            'echo Logging in to Amazon ECR...',
            '$(aws ecr get-login --no-include-email --region $AWS_DEFAULT_REGION)',
          ]
        },
        build: {
          commands: [
            'echo Build started on `date`',
            'echo Building the Docker image...',
            `docker build -t ${repositoryUri}:$CODEBUILD_RESOLVED_SOURCE_VERSION .`,
          ]
        },
        post_build: {
          commands: [
            'echo Build completed on `date`',
            'echo Pushing the Docker image...',
            `docker push ${repositoryUri}:$CODEBUILD_RESOLVED_SOURCE_VERSION`,
          ]
        },
      },
    });
  }
}

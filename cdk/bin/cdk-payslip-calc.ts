#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from '@aws-cdk/core';
import { CdkPayslipCalcStack } from '../lib/cdk-payslip-calc-stack';

const app = new cdk.App();
new CdkPayslipCalcStack(app, 'CdkPayslipCalcStack', {
  env : {
    account: "527531474351",
    region: "us-east-2",
  }
});

app.synth();

package com.yenthefromghent.sjls.core.lsp.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Capabilities {
    public Boolean supportsConfigurationDoneRequest = false;
    public Boolean supportsFunctionBreakpoints = false;
    public Boolean supportsConditionalBreakpoints = false;
    public CompletionOptions supportsCompletionProviver = null;
    public Boolean supportsHitConditionalBreakpoints = false;
    public Boolean supportsEvaluateForHovers = false;
    public Boolean supportsStepBack = false;
    public Boolean supportsSetVariable = false;
    public Boolean supportsRestartFrame = false;
    public Boolean supportsGotoTargetsRequest = false;
    public Boolean supportsStepInTargetsRequest = false;
    public Boolean supportsCompletionsRequest = false;
    public Boolean supportsModulesRequest = false;
    public String[] supportedChecksumAlgorithms = null;
    public Boolean supportsRestartRequest = false;
    public Boolean supportsExceptionOptions = false;
    public Boolean supportsValueFormattingOptions = false;
    public Boolean supportsExceptionInfoRequest = false;
    public Boolean supportTerminateDebuggee = false;
    public Boolean supportsDelayedStackTraceLoading = false;
    public Boolean supportsLoadedSourcesRequest = false;
    public Boolean supportsLogPoints = false;
    public Boolean supportsTerminateThreadsRequest = false;
    public Boolean supportsSetExpression = false;
    public Boolean supportsTerminateRequest = false;
    public Boolean supportsDataBreakpoints = false;
    public Boolean supportsReadMemoryRequest = false;
    public Boolean supportsDisassembleRequest = false;
}

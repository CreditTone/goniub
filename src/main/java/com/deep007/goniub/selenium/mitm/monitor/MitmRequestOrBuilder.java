// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: mitm_flow.proto

package com.deep007.goniub.selenium.mitm.monitor;

public interface MitmRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:mitm.MitmRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.mitm.MitmBinding mitmBinding = 1;</code>
   */
  boolean hasMitmBinding();
  /**
   * <code>.mitm.MitmBinding mitmBinding = 1;</code>
   */
  com.deep007.goniub.selenium.mitm.monitor.MitmBinding getMitmBinding();
  /**
   * <code>.mitm.MitmBinding mitmBinding = 1;</code>
   */
  com.deep007.goniub.selenium.mitm.monitor.MitmBindingOrBuilder getMitmBindingOrBuilder();

  /**
   * <code>string url = 2;</code>
   */
  java.lang.String getUrl();
  /**
   * <code>string url = 2;</code>
   */
  com.google.protobuf.ByteString
      getUrlBytes();

  /**
   * <code>string method = 3;</code>
   */
  java.lang.String getMethod();
  /**
   * <code>string method = 3;</code>
   */
  com.google.protobuf.ByteString
      getMethodBytes();

  /**
   * <code>repeated .mitm.MitmHeader headers = 4;</code>
   */
  java.util.List<com.deep007.goniub.selenium.mitm.monitor.MitmHeader> 
      getHeadersList();
  /**
   * <code>repeated .mitm.MitmHeader headers = 4;</code>
   */
  com.deep007.goniub.selenium.mitm.monitor.MitmHeader getHeaders(int index);
  /**
   * <code>repeated .mitm.MitmHeader headers = 4;</code>
   */
  int getHeadersCount();
  /**
   * <code>repeated .mitm.MitmHeader headers = 4;</code>
   */
  java.util.List<? extends com.deep007.goniub.selenium.mitm.monitor.MitmHeaderOrBuilder> 
      getHeadersOrBuilderList();
  /**
   * <code>repeated .mitm.MitmHeader headers = 4;</code>
   */
  com.deep007.goniub.selenium.mitm.monitor.MitmHeaderOrBuilder getHeadersOrBuilder(
      int index);

  /**
   * <code>bytes content = 5;</code>
   */
  com.google.protobuf.ByteString getContent();
}

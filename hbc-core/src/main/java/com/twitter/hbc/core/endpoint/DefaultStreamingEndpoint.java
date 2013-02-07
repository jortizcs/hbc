/**
 * Copyright 2013 Twitter, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package com.twitter.hbc.core.endpoint;

import com.twitter.hbc.core.Constants;

/**
 * A basic endpoint object
 */
public class DefaultStreamingEndpoint extends BaseEndpoint implements StreamingEndpoint {

  protected final boolean backfillable;
  private volatile boolean delimited;
  /**
   * All endpoints have delimited=length by default
   * @param path must start with "/". Should not contain the api version
   * @param backfillable true if this endpoint can accept the "count" param for backfilling
   */
  public DefaultStreamingEndpoint(String path, String httpMethod, boolean backfillable) {
    super(path, httpMethod);
    this.backfillable = backfillable;
    this.delimited = true;
  }

  public boolean isBackfillable() {
    return backfillable;
  }

  @Override
  protected void addDefaultParams() {
    delimited(this.delimited);
  }

  public final void delimited(boolean on) {
    if (on) {
      addQueryParameter(Constants.DELIMITED_PARAM, Constants.DELIMITED_VALUE);
      delimited = true;
    } else {
      removeQueryParameter(Constants.DELIMITED_PARAM);
      delimited = false;
    }
  }

  public final void stallWarnings(boolean on) {
    if (on) {
      addQueryParameter(Constants.STALL_WARNING_PARAM, Constants.STALL_WARNING_VALUE);
    } else {
      removeQueryParameter(Constants.STALL_WARNING_PARAM);
    }
  }

  @Override
  public final void setBackfillCount(int backfillCount) {
    if (isBackfillable()) {
      if (backfillCount != 0) {
        addQueryParameter(Constants.COUNT_PARAM, Integer.toString(backfillCount));
      } else {
        removeQueryParameter(Constants.COUNT_PARAM);
      }
    }
  }
}

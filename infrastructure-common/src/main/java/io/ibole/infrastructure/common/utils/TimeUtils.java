package io.ibole.infrastructure.common.utils;

import java.time.Duration;
import java.util.Date;

/*********************************************************************************************.
 * 
 * 
 * <p>版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
 * 
 * <p></p>
 *********************************************************************************************/

/**
 * Utility methods for dealing with time. Particularly duration.
 * 
 * @author bwang (chikaiwang@hotmail.com)
 *
 */
public final class TimeUtils {

  /**

   * Private constructor for utility class.

   */

  private TimeUtils() {

  }

  /**
   * Get the duration between when something was started and finished.
   *
   * @param started  The start time. Can be null will automatically set the duration to 0
   * @param finished The finish time. If null will use (current time - started time) to get the duration
   * @return The duration or zero if no duration
   */
  public static Duration getDuration(final Date started, final Date finished) {

      if (started == null || started.getTime() == 0) {

          // Never started

          return Duration.ZERO;

      } else if (finished == null || finished.getTime() == 0) {

          // Started but hasn't finished

          return Duration.ofMillis(new Date().getTime() - started.getTime());

      } else {

          // Started and finished

          return Duration.ofMillis(finished.getTime() - started.getTime());

      }

  }

}

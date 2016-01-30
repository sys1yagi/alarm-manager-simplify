# Alarm Manager Simplify

[![Circle CI](https://circleci.com/gh/sys1yagi/alarm-manager-simplify.svg?style=svg)](https://circleci.com/gh/sys1yagi/alarm-manager-simplify)

Alarm Manager Simplify is a code generation library to simplify the code for AlarmManager on Android.

Handling of AlarmManager's complicated. PendingIntent, WakefulBroadcastReceiver, IntentService...

## Architecture

<img src="https://raw.githubusercontent.com/sys1yagi/alarm-manager-simplify/master/art/architecture.png" width="500px"/>

## How to use

### Implement AlarmProcessor and add annotation

```java
@AlarmName("SimpleAction")
public class SimpleActionProcessor implements AlarmProcessor {
  @Override
  public void process(Context context, Intent intent) {
    Log.d("SimpleAction", "SimpleAction do");
  }
}
```

```java
SimpleActionProcessorScheduler.scheduleRtcWakeup(context, 1000);
```

```java
public class SimpleActionProcessorScheduler {

    public static void scheduleRtcWakeup(Context context, int target) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar now = Calendar.getInstance();
        now.add(Calendar.MILLISECOND, target);

        Intent intent = new Intent(context, SimplifiedAlarmReceiver.class);
        intent.putExtra("event", "SimpleAction");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);
        alarmManager.cancel(pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), pendingIntent);
    }

    //...
}
```

### Installation

This library is distributed by [JitPack](https://jitpack.io/). Add dependencies your build.gradle

```
TODO
```

## Development


__Show version__

```
$ ./gradlew version
```

__Bump version__

```
$ ./gradlew bumpMajor
$ ./gradlew bumpMinor
$ ./gradlew bumpPatch
```

__Generate README__

```
$ ./gradlew genReadMe
```


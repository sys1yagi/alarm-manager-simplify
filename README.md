# Alarm Manager Simplify

[![Circle CI](https://circleci.com/gh/sys1yagi/alarm-manager-simplify.svg?style=svg)](https://circleci.com/gh/sys1yagi/alarm-manager-simplify)

Alarm Manager Simplify is a code generation library to simplify the code for AlarmManager on Android.

Handling of AlarmManager's complicated. PendingIntent, WakefulBroadcastReceiver, IntentService...

## Architecture

<img src="https://raw.githubusercontent.com/sys1yagi/alarm-manager-simplify/master/art/architecture.png" width="500px"/>

## How to use

### Implement AlarmProcessor and add annotation

```java
@Simplify("SimpleAction")
public class SimpleActionProcessor implements AlarmProcessor {
  @Override
  public void process(Context context, Intent intent) {
    Log.d("SimpleAction", "SimpleAction do");
  }
}
```

### Add to AndroidManifest.xml


```xml
<uses-permission android:name="android.permission.WAKE_LOCK"/>

<application>
  <receiver android:name="com.sys1yagi.android.alarmmanagersimplify.SimplifiedAlarmReceiver"
          android:exported="false"/>
  <service android:name="com.sys1yagi.android.alarmmanagersimplify.SimplifiedAlarmService"
         android:exported="false"/>
</application>
```

### Schedule alarm.

Set up a alarm using generated Scheduler.

```java
SimpleActionProcessorScheduler.scheduleRtcWakeup(context, 1000);
```

### Installation

This library is distributed by [JitPack](https://jitpack.io/). Add dependencies your build.gradle

```
apt 'com.github.sys1yagi.alarm-manager-simplify:processor:0.2.0'
compile 'com.github.sys1yagi.alarm-manager-simplify:library:0.2.0'
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


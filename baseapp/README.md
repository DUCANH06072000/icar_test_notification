###### <div align="center">  BASE PROJECT  </div><br>

##<div>Trong dependencies:</div>
    *<div>implementation project(':baseapp')*<br>

##<div>Trong settings.gradle:</div>
    Add: *include ':baseapp'*<br>
    Add repositories: *maven { url "https://jitpack.io" }*<br>

##<div>Trong proguard-rules.pro:</div>
    - Bổ xung * -keepclassmembers public class com.tatv.baseapp.utils.log.Log*<br>
    - Bổ xung * -keepclassmembers public class com.tatv.baseapp.utils.location.ILocation*<br>

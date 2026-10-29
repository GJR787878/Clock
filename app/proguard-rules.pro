# 基础混淆规则
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# 保留注解
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

# 不混淆本地代码
-keepclasseswithmembernames class * {
    native <methods>;
}

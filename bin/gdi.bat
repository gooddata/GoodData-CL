SET PRJ_BIN=%~dp0
echo %PRJ_BIN%
%JAVA_HOME%\bin\java.exe  -Xmx256M -Dlog4j.configuration=%PRJ_BIN%\..\log4j.configuration -Dderby.system.home=%PRJ_BIN%\..\db -Djava.io.tmpdir=%PRJ_BIN%/../tmp -classpath %PRJ_BIN%\..;%PRJ_BIN%\..\target\GoodDataDI-0.6-jar-with-dependencies.jar com.gooddata.processor.GdcDI %*
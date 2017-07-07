TEMPLATE = app
QT += quick qml

QT += androidextras
ANDROID_PACKAGE_SOURCE_DIR = $$PWD/android-sources

LIBS += -ljnigraphics  -llog -lGLESv2 -lEGL  -landroid -lm  -pthread -std=c++11

SOURCES += *.cpp
HEADERS += *.h
RESOURCES += rendercontrol.qrc

OTHER_FILES += \
    android-sources/src/com/totos/run/to/coins/*.java \
    android-sources/AndroidManifest.xml \
android-sources/libs/openmaps.jar \
android-sources/res/layout/activity_openglndk.xml \
android-sources/res/values/*.xml \




#target.path = $$[QT_INSTALL_EXAMPLES]/quick/rendercontrol
INSTALLS += target



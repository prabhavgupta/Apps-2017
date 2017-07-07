import QtQuick 2.0
import QtQuick.Window 2.0
import QtQuick.Particles 2.0

Text {
    id: distance_id

    state: "NORMAL"
    text: "Distance:"
    objectName: "objectDistance"
    font.pointSize: Screen.height/30
    x: parent.width*1/5
    y: parent.height*9/10
    color:"white"


}

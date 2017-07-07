import QtQuick 2.7
import QtQuick.Controls 2.0
import QtQuick.Layouts 1.0


Item{
    id:principal
    objectName: "up"

    x:parent.width/2
    visible:true
    scale:3


    Text {
        id:textUp
        color: "#ea0303"
        text: "up"
        styleColor: "#fdb0a6"
        font.pointSize: 14
        style: Text.Sunken
        font.family: "Tahoma"
        transformOrigin: Item.Center
        wrapMode: Text.WordWrap
        font.italic: true
        horizontalAlignment: Text.AlignHCenter
        font.bold: true

    }
    Image {
        id: up
        x: 0
        y: textUp.y+10
        source: "up.png"
        OpacityAnimator {
                 target: up;
                 from: 0.0;
                 to: 0.5;
                 duration: 2000
                 running: true
                 loops: Animation.Infinite
             }


        NumberAnimation on y {
            from: up.y + 50
            to: textUp.y+10
            duration: 2000
           loops: Animation.Infinite
        }
     }
}

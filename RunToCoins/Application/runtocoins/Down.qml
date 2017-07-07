import QtQuick 2.7
import QtQuick.Controls 2.0
import QtQuick.Layouts 1.0


Item{
    id:principal
    objectName: "down"

    x:parent.width/2
    visible:true
    scale:3


    Text {
        id:textDown
        color: "#ea0303"
        text: "down"
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
        id: down
        x: 0
        y: textDown.y+10
        source: "down.png"
        OpacityAnimator {
                 target: down;
                 from: 0.0;
                 to: 0.5;
                 duration: 2000
                 running: true
                 loops: Animation.Infinite
             }


        NumberAnimation on y {
            from: textDown.y+10
            to: down.y + 50
            duration: 2000
           loops: Animation.Infinite
        }
    }
}

import QtQuick 2.7
import QtQuick.Controls 2.0
import QtQuick.Layouts 1.0
import QtQuick.Window 2.0

Item{
    id:contenedor
    visible:true
    objectName: "touch_me"

    property int contadorMonedas:0
    property real posicionx: parseInt(Math.random() * (Screen.width-(Screen.width/10) ))
    property real posiciony: parseInt(Math.random() * (Screen.height -(Screen.height/10)))
    Text{ id:textTouch
        color: "blue"
        text: "Touch me"
        styleColor: "Yellow"
        font.pointSize: 20
        style: Text.Sunken
        font.family: "Tahoma"
        font.italic: true
        horizontalAlignment: Text.AlignHCenter
        font.bold: true
        x:posicionx
        y:posiciony

    }
    Touchme{widthCircle: 110;heightCircle:  110; radiusCircle:  110;x:posicionx+20;y:posiciony+20}
    Touchme{widthCircle: 130;heightCircle:  130; radiusCircle:  130;x:posicionx+10;y:posiciony+10}
    Touchme{widthCircle: 150;heightCircle:  150; radiusCircle:  150;x:posicionx;y:posiciony;
        MouseArea{
            anchors.leftMargin: -5
            anchors.topMargin: 0
            anchors.rightMargin: -281
            anchors.bottomMargin: -263
            anchors.fill: parent
            onClicked: {
             posicionx = parseInt(Math.random() * (Screen.width-(Screen.width/10) ))
             posiciony = parseInt(Math.random() * (Screen.height-(Screen.height/10)))
               // console.log("strImage " +posicionx+" "+posiciony+" "+Math.random());


             //if(contadorMonedas>2)
             {
                // contenedor.enabled = false
             contenedor.visible = false
             }
             contadorMonedas = contadorMonedas+1



         }
}
    }

}

import QtQuick 2.0
import QtQuick.Window 2.0
import QtQuick.Particles 2.0

Text {
    id: score
    width: 200; height:Screen.height/11
    state: "NORMAL"
    text: "Score"
    objectName: "objectText"
    font.pointSize: Screen.height/20
    color:"white"

    property bool corre:false

        Timer {
            interval: 500
            triggeredOnStart: true
            running: true
            repeat: true
            onTriggered: {
            //! [0]
                if(corre) {
                    if (score.state == "NORMAL")
                        score.state = "CRITICAL"
                    else
                        score.state = "NORMAL"
                    sys.resume()
                    corre=false
                }


            }
        }

    states: [
        State {
            name: "NORMAL"
            PropertyChanges { target: score;scale:1;style: Text.Raised;styleColor: "#ffffffff";font.bold: false;color:"#ffffffff"}


                  },
        State {
            name: "CRITICAL"
            PropertyChanges { target: score; scale:1.2;style: Text.Sunken; styleColor: "#657072";font.bold: true;color:"#b0b7b8"}




        }
    ]

    transitions: [
    Transition {
        from: 'NORMAL'; to: 'CRITICAL'
        ParentAnimation {
            target: score
            NumberAnimation { properties: 'scale.color'; duration: 500; }
        }
    },
    Transition {
            from: 'CRITICAL'; to: 'NORMAL'
            ParentAnimation {
                target: score
                NumberAnimation { properties: 'scale,color'; duration: 500; }
            }
        }
    ]


   /* MouseArea {
        id:mouseArea
        anchors.bottomMargin: score
        anchors.fill: score
        onClicked: {
            if (score.state == "NORMAL")
                score.state = "CRITICAL"
            else
                score.state = "NORMAL"
        }
        onPressed: sys.resume()
    }*/


       ParticleSystem {
                id: sys
                anchors.fill: parent
                onEmptyChanged: if (empty) sys.pause();

                ImageParticle {
                    system: sys
                    id: cp
                    source: "palabra.png"
                    colorVariation: 0.8
                    color: "#5F000FAA"
                }




    Emitter {
        id: bursty
        system: sys
         enabled: corre
        x: score.width/2.0//mouseArea.mouseX
        y: score.height/2.0//mouseArea.mouseY
        emitRate: 16000
        maximumEmitted: 4000
        acceleration: AngleDirection {angleVariation: 360; magnitude: 360; }
        size: 8
        endSize: 16
        sizeVariation: 4

    }
   }


}



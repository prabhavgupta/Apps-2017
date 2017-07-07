import QtQuick 2.0
import QtQuick.Particles 2.0

Item {
property bool corre:false
    objectName: "startCoin"
    property real control: 0
    width: parent.width
    height: parent.height
    id: startCoin


    Timer {
        interval: 500
        triggeredOnStart: true
        running: true
        repeat: true
        onTriggered: {
        //! [0]
            if(corre) {
                pulseEmitter.pulse(500);
                control++

            }


        }
    }

    ParticleSystem {
        id: particles
        anchors.fill: parent
        ImageParticle {
            source: "qrc:///particleresources/star.png"
            alpha: 0
            colorVariation: 0.6
        }

      /*  Emitter {
            id: burstEmitter
            x: parent.width/2
            y: parent.height/3
            emitRate: 1000
            lifeSpan: 2000
            enabled: false
            velocity: AngleDirection{magnitude: 64; angleVariation: 360}
            size: 24
            sizeVariation: 8
            Text {
                anchors.centerIn: parent
                color: "white"
                font.pixelSize: 18
                text: "Burst"
            }
        }*/
        Emitter {
            id: pulseEmitter
            x: startCoin.width/2
            y: startCoin.height/2
            emitRate: 1000
            lifeSpan: 2000
            enabled: corre
            velocity: AngleDirection{magnitude: 120; angleVariation: 360}
            size: 24
            sizeVariation: 8

        }
    }

}

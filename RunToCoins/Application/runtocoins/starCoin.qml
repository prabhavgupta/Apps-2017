import QtQuick 2.0
import QtQuick.Particles 2.0

Item {

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
            x: parent.width/2
            y: 2*parent.height/3
            emitRate: 1000
            lifeSpan: 2000
            enabled: true
            velocity: AngleDirection{magnitude: 64; angleVariation: 360}
            size: 24
            sizeVariation: 8

        }
    }

}

import QtQuick 2.0
import QtQuick.Particles 2.0
import QtQuick.Window 2.0

Rectangle {
    id: root
    width:Screen.width ; height:Screen.height
    gradient: Gradient{
        GradientStop{ position: 0.0; color:Qt.rgba(.1,.8,1,1) }
        GradientStop{ position: 1.0; color:Qt.rgba(0,0,0,1) }
    }


    ParticleSystem {
        id: particleSystem
    }

    Emitter {
        id: emitter
        anchors.centerIn: parent
        width:Screen.width; height:Screen.height
        system: particleSystem
        emitRate: 10
        lifeSpan: 1000
        lifeSpanVariation: 5000
        size: 16
        endSize: 32
        velocity: AngleDirection {
            angle: 90
            angleVariation: 100
            magnitude: 200
                magnitudeVariation: 800
        }
        acceleration: AngleDirection {
            angle: 100
            magnitude: 250
        }


    }

    ImageParticle {
        source: "tarde.png"
        system: particleSystem
                color: Qt.rgba(1,1,1,.3)
                colorVariation: 0.3
                rotation: 10
                rotationVariation: 45
                rotationVelocity: 15
                rotationVelocityVariation: 15
                entryEffect: ImageParticle.Scale
                 alpha: 1.0

    }

}

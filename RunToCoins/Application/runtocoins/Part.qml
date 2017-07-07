import QtQuick 2.0
import QtQuick.Particles 2.0

Item {

    id: root

    height: parent.height
    width: parent.width
    objectName: "particleCoin"
    property real posxC: 0
    property real posyC: 0
    x:posxC
    y:posyC


    MouseArea {
        id: mouseArea
        anchors.fill: root
    }

    ParticleSystem { id: sys1 }
    ImageParticle {
        system: sys1
        source: "qrc:///particleresources/glowdot.png"
        color: "violet"
        alpha: 0
        SequentialAnimation on color {
            loops: Animation.Infinite


            ColorAnimation {
                from: "magenta"
                to: "violet"
                duration: 2000
            }
            ColorAnimation {
                from: "violet"
                to: "magenta"
                duration: 2000
            }
        }
     //   colorVariation: 0.3
    }
    //! [0]
    Emitter {
        id: trailsNormal
        system: sys1

        emitRate: 500
        lifeSpan: 2000

        y: mouseArea.pressed ? mouseArea.mouseY : circle.cy
        x: mouseArea.pressed ? mouseArea.mouseX : circle.cx

        velocity: PointDirection {xVariation: 4; yVariation: 4;}
        acceleration: PointDirection {xVariation: 10; yVariation: 10;}
        //velocityFromMovement: 8

        size: 8
        sizeVariation: 4
    }
    //! [0]
    ParticleSystem { id: sys2 }
    ImageParticle {
        color: "violet"
        system: sys2
        alpha: 0
        x:root.posxC
        y:root.posyC


        SequentialAnimation on color {
            loops: Animation.Infinite


            ColorAnimation {
                from: "magenta"
                to: "violet"
                duration: 1000
            }
            ColorAnimation {
                from: "violet"
                to: "magenta"
                duration: 2000
            }
        }

       // colorVariation: 0.5
        source: "qrc:///particleresources/star.png"
    }
    Emitter {
        id: trailsStars
        system: sys2

        emitRate: 100
        lifeSpan: 2200


        y:  circle.cy
        x:  circle.cx

        velocity: PointDirection {xVariation: 4; yVariation: 4;}
       // acceleration: PointDirection {xVariation: 10; yVariation: 10;}
       // velocityFromMovement: 8

        size: 22
        sizeVariation: 4
    }


    Item {
        id: circle
        //anchors.fill: parent
        property real radius: 0
        property real dx: root.posxC
        property real dy: root.posyC
        property real cx: radius * Math.sin(percent*6.283185307179)
        property real cy: radius * Math.cos(percent*6.283185307179)
        property real percent: 0
        x:dx
        y:dy

        SequentialAnimation on percent {
            loops: Animation.Infinite
            running: true
            NumberAnimation {
            duration: 1000
            from: 1
            to: 0
            loops: 8
            }

        }

        SequentialAnimation on radius {
            loops: Animation.Infinite
            running: true
            NumberAnimation {
                duration: 4000
                from: 0
                to: 100
            }
        }
    }



}

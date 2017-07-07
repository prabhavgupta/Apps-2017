import QtQuick 2.7
import QtQuick.Layouts 1.0


Item {
    id: principal
     property real  heightCircle: 150
    property real widthCircle: 150
    property int radiusCircle: 150
    property color colorCircle:"transparent"



    Rectangle{
        id: touch

        color: colorCircle
        width:principal.widthCircle
        height: principal.heightCircle
        radius: principal.radiusCircle
        border.color: "#0B6138"



        ParallelAnimation {
            running: true
            NumberAnimation  {
                target: touch
                property: "scale"
                from: 0.5
                to: 2
                duration: 1000

              }
            NumberAnimation  {
                target: touch
                property: "border.width"
                from: 1
                to: 5
                duration: 1000

              }


            loops: Animation.Infinite
        }

        OpacityAnimator {
                 target: touch;
                 from: 0.0;
                 to: 5.0;
                 duration: 5000
                 running: true
                 loops: Animation.Infinite
             }

        ColorAnimation on border.color {
            from: "#0B6138"
            to: "#64FE2E"
            duration: 1000
             loops: Animation.Infinite
        }





    }

}

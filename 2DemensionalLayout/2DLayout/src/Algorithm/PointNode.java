package Algorithm;

/**
 * 记录节点的位置信息链表
 * @author Archer
 */
public class PointNode {
    private double pointX;
    private double pointY;
    private double nodeLength;


    public PointNode() {
    }

    public PointNode(double pointX, double pointY, double nodeLength) {
        this.pointX = pointX;
        this.pointY = pointY;
        this.nodeLength = nodeLength;

    }

    public PointNode(PointNode pointNode) {
        this.pointX = pointNode.getPointX();
        this.pointY = pointNode.getPointY();
        this.nodeLength = pointNode.getNodeLength();
    }

    public double getPointX() {
        return pointX;
    }

    public double getPointY() {
        return pointY;
    }

    public double getNodeLength() {
        return nodeLength;
    }

    public void setPointX(double pointX) {
        this.pointX = pointX;
    }

    public void setPointY(double pointY) {
        this.pointY = pointY;
    }

    public void setNodeLength(double nodeLength) {
        this.nodeLength = nodeLength;
    }


    @Override
    public String toString() {
        return "PointNode:" +
                "X=" + pointX +
                ", Y=" + pointY +
                ", Length=" + nodeLength;
    }
}

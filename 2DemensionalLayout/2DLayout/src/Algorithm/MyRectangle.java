package Algorithm;

/**
 * @author Archer
 */
public class MyRectangle {
    private double width;
    private double height;


    public MyRectangle(MyRectangle myRectangle) {
        this.width = myRectangle.getWidth();
        this.height = myRectangle.getHeight();

    }

    public MyRectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public double getArea() {
        return this.width * this.height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "width=" + width + ", height=" + height;
    }

    /**
     *
     * @return 返回矩形最小边
     */
    public double minlength(){
        return Math.max(this.getWidth(), this.getHeight());
    }
}

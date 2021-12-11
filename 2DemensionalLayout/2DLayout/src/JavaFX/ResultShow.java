package JavaFX;

import Algorithm.Anneal;
import Algorithm.MyRectangle;
import Algorithm.PointNode;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import Algorithm.Data;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Archer
 */
public class ResultShow extends Application {
    public static void main(String[] args) throws IOException {
        launch(args);
    }

    public String createColor() {
        //红色
        String red;
        //绿色
        String green;
        //蓝色
        String blue;
        //生成随机对象
        Random random = new Random();
        //生成红色颜色代码
        red = Integer.toHexString(random.nextInt(256)).toUpperCase();
        //生成绿色颜色代码
        green = Integer.toHexString(random.nextInt(256)).toUpperCase();
        //生成蓝色颜色代码
        blue = Integer.toHexString(random.nextInt(256)).toUpperCase();

        //判断红色代码的位数
        red = red.length() == 1 ? "0" + red : red;
        //判断绿色代码的位数
        green = green.length() == 1 ? "0" + green : green;
        //判断蓝色代码的位数
        blue = blue.length() == 1 ? "0" + blue : blue;
        //生成十六进制颜色值
        String color = "#" + red + green + blue;
        return color;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Anneal main = new Anneal();
        main.initData(400, 400, 100, "D:\\桌面\\2DemensionalLayout\\RectData.txt");
        Data best = main.anneal();
        ArrayList<PointNode> points = best.getPointList();
        ArrayList<MyRectangle> rectangles = best.getBestRectangleTale();
        //根据大矩形的宽高设置画布大小
        Canvas canvas = new Canvas(best.getWidth() * 2, best.getHeight() * 2);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Group root = new Group();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        for (int i = 0; i < rectangles.size(); i++) {
            double x = points.get(i).getPointX();
            double y = points.get(i).getPointY();
            double width = rectangles.get(i).getWidth();
            double height = rectangles.get(i).getHeight();
            //在画布上画矩形
            gc.setFill(Paint.valueOf(createColor()));
            gc.fillRect(x, y, width, height);
            gc.setFont(Font.font(10));
            gc.setFill(Paint.valueOf("black"));
            gc.fillText(width + "x\n" + height, x, y + 10);
        }

        stage.setTitle("2D排样显示图 ");
        stage.setWidth(best.getWidth() +20);
        stage.setHeight(best.getHeight() + 50);
        stage.show();
//        stage.setMaxWidth(init.getWidth());
//        stage.setMaxHeight(init.getHeight());

    }
}

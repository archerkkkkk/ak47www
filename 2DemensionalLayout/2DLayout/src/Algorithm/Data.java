package Algorithm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;


/**
 * version1:
 * 处理文件数据读取和初始化，更新
 * //按照数组里的顺序开始放置矩形，可放置位置就按节点链表来找
 * 思路是：
 * 1、先看width，height是否超过，没有就放置，超过就找下一个矩形看看能否放置，
 * 剩余都不能就把该节点归并到前一个节点（删除该节点并延长上一个节点长度），这样就完成一层的排样
 * 2、关于放置节点，就选取y最小的节点进行放置，
 * 3、然后利用打分机制来选择矩形进行实际放置，并且更新节点链表（先变节点，判断新节点能否再放置）
 * 打分机制：(int)((小矩形的宽/位置的长度)*100)/10
 * <p>
 * version2
 * 更新矩形数组，用交换来进行,记下节点数组，
 * <p>
 * version3
 * 学习Javafx进行图形化，创建
 * <p>
 * 排样率(layoutRate)：所利用空间占总空间的百分比
 * 使用率(useRate)：所生产小矩形数量与所有矩形数量的百分比
 *
 * @author Archer
 */
public class Data {

    private double width = 0;
    private double height = 0;
    private int rectangleNum; //小矩形数目
    private ArrayList<MyRectangle> rectangleTable; //矩形表
    private ArrayList<MyRectangle> bestRectangleTale; //实际排样放进去的矩形表
    private ArrayList<PointNode> pointPath; // LinkedList
    private int minIndex;
    private double minLength = 100000;//矩形表的的最小边
    private ArrayList<PointNode> pointList;

    public double getMinLength() {
        return minLength;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public ArrayList<MyRectangle> getBestRectangleTale() {
        return bestRectangleTale;
    }

    public ArrayList<MyRectangle> getRectangleTale() {
        return rectangleTable;
    }

    public ArrayList<PointNode> getPointList() {
        return pointList;
    }

    public double calculateUseRate() {
        return (double) bestRectangleTale.size() / rectangleTable.size();
    }

    public void printUseRate() {
        System.out.println("样品使用率为：" + (double) bestRectangleTale.size() / rectangleTable.size() * 100 + "%");
    }

    public void printLayoutRate() {
        double allArea = 0;
        for (MyRectangle rect : bestRectangleTale) {
            allArea += rect.getArea();
        }

        System.out.println("排样率为：" + allArea / (width * height) * 100 + "%");
    }

    public double calculateLayoutRate() {
        double allArea = 0;
        for (MyRectangle rect : bestRectangleTale) {
            allArea += rect.getArea();
        }

        return allArea / (width * height);
    }

    public Data() {
        this.rectangleTable = new ArrayList<MyRectangle>();
        this.pointPath = new ArrayList<PointNode>();
        pointPath.add(new PointNode(0, 0, this.width));
    }

    /**
     * 构造函数，顺便把链表的头节点指定
     *
     * @param width        大矩形宽
     * @param height       大矩形高
     * @param rectangleNum 矩形数量
     */
    public Data(double width, double height, int rectangleNum) {
        this.width = width;
        this.height = height;
        this.rectangleNum = rectangleNum;
        //这里一定要记得初始化，没有实例化对象，否则报空指针异常
        rectangleTable = new ArrayList<MyRectangle>();
        bestRectangleTale = new ArrayList<MyRectangle>();
        //先把零节点加入，方便后面记录顺序，还要进行更新
        pointPath = new ArrayList<PointNode>();
        pointPath.add(new PointNode(0, 0, this.width));
        pointList = new ArrayList<PointNode>();
    }

    public Data(Data date) {
        this.width = date.getWidth();
        this.height = date.getHeight();
        this.pointList = new ArrayList<>(date.getPointList());
        this.rectangleTable = new ArrayList<>(date.getRectangleTale());
        this.bestRectangleTale = new ArrayList<>(date.getBestRectangleTale());
        pointPath = new ArrayList<PointNode>();
        pointPath.add(new PointNode(0, 0, date.getWidth()));
        this.minLength = date.getMinLength();
    }

    /**
     * @param filepath 数据文件路径
     * @throws IOException 异常处理
     */
    public void readData(String filepath) throws IOException {

        double[] width = new double[rectangleNum];
        double[] height = new double[rectangleNum];
        String substring;
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(filepath)));
        //获取每个小矩形的宽和高
        for (int i = 0; i < rectangleNum; i++) {
            substring = br.readLine();
            String[] strSplit = substring.split(" ");
            width[i] = Integer.parseInt(strSplit[0]);
            height[i] = Integer.parseInt(strSplit[1]);
            //读数据的时候就获取最小矩形边
            if (minLength > width[i]) {
                minLength = width[i];
            }
            //将读取到的矩形先放进rectangleTable里
            MyRectangle tempRect = new MyRectangle(width[i], height[i]);
            rectangleTable.add(tempRect);
        }
        //关闭字符流
        br.close();
    }

    /**
     * 随意选择一个矩形作为起始放置在（0，0）点
     */
    public void layout() {
        int count = 0;
        int current = 0;// 当前位置
        //先建立节点，每次遍历先更新两个节点指向
        PointNode new1 = new PointNode();
        PointNode new2 = new PointNode();
        //根据矩形表顺序进行遍历
        for (; current < rectangleTable.size(); current++) {

            //寻找当前最小节点
            minIndex = minIndex(pointPath);
            //System.out.println("最小节点" + minIndex + "\t" + pointPath.get(minIndex));
            PointNode lowPointNode = pointPath.get(minIndex);

            //找到打分最高矩形并交换，此时当前下标current
            scoreRect(lowPointNode, current);
            MyRectangle temp = rectangleTable.get(current);
            //System.out.println("当前矩形宽高：width=" + temp.getWidth() + "  height=" + temp.getHeight());

            //预备两个新节点，坐标是现节点（y最小的节点）的基础加矩形的数据
            //1节点是y向变化
            new1.setPointX(lowPointNode.getPointX());
            new1.setPointY(lowPointNode.getPointY() + temp.getHeight());
            new1.setNodeLength(temp.getWidth());  //这里以后要根据矩形怎么放来决定
            //2节点是x向变化,节点长度=现节点长度减矩形边长
            new2.setPointX(lowPointNode.getPointX() + temp.getWidth());
            new2.setPointY(lowPointNode.getPointY());//这里以后要根据矩形怎么放来决定
            new2.setNodeLength(lowPointNode.getNodeLength() - temp.getWidth());
            if (temp.getWidth() <= lowPointNode.getNodeLength()) {
                // 要看最小矩形边，能再放就继续放，不能就把这个点合并到new1节点上(可以递归)
                if (minLength > new2.getNodeLength() && new1.getPointY() <= height && new2.getPointX() <= width) {
                    pointList.add(lowPointNode);
                    //如果new2的长度不能再放，就先归并到new1
                    new1.setNodeLength(lowPointNode.getNodeLength());
                    pointPath.set(minIndex, new PointNode(new1));
                    bestRectangleTale.add(temp);
                    count++;//成功放置
                    //归并后将new1加入节点列表
                } else if (minLength <= new2.getNodeLength() && new1.getPointY() <= height && new2.getPointX() <= width) {
                    //上面找到的矩形是小于当前最小节点的高分矩形
                    pointList.add(lowPointNode);
                    //到这里有一个处理,更新链表,因为new2只做x向变化所以就是新的最低点
                    pointPath.set(minIndex, new PointNode(new1));
                    pointPath.add(minIndex + 1, new PointNode(new2));
                    bestRectangleTale.add(temp);
                    count++;//成功放置
                }
            }

        }
//        System.out.println("节点数量" + pointList.size());
//        printPointList();
//        System.out.println("--------------");
//        System.out.println("放置矩形数量：" + bestRectangleTale.size());
//        printBestRectangleTable();

    }


    /**
     * @param pointPath 节点链表
     * @return Y值最小节点
     */
    public int minIndex(ArrayList<PointNode> pointPath) {
        double y = height;
        int index = 0;
        for (int i = 0; i < pointPath.size(); i++) {
            if (pointPath.get(i).getPointY() < y) {
                y = pointPath.get(i).getPointY();
                index = i;
            }
        }
        return index;
    }

    public void generateNeighourTour() {
        Collections.shuffle(rectangleTable);
        this.bestRectangleTale.clear();
        this.pointList.clear();
        this.pointPath.clear();
        this.pointPath.add(new PointNode(0, 0, width));
        layout();
    }

    public void printPointNode() {
        for (PointNode ps : pointPath) {
            System.out.println(ps);
        }
    }

    public void printPointList() {
        for (PointNode ps : pointList) {
            System.out.println(ps);
        }
    }

    public void printBestRectangleTable() {
        for (MyRectangle rect : bestRectangleTale) {
            System.out.println(rect);
        }
    }

    public void printRectangleTable() {
        for (MyRectangle rect : rectangleTable) {
            System.out.println(rect);
        }
    }


    public void scoreRect(PointNode pointNode, int index) {
        double max = 0;
        int scoreIndex = 0;
        for (int j = index; j < rectangleTable.size() - index; j++) {
            //评分，找到最高分矩形并交换当前的矩形,这里的问题是要找到评分最高但是不超过10分，但是取整会把10.4也归到10
            int score = (int) (rectangleTable.get(j).getWidth() / pointNode.getNodeLength() * 100) / 10;
            if (score > max && rectangleTable.get(j).getWidth() / pointNode.getNodeLength() <= 1.0) {
                max = score;
                scoreIndex = j;
            }
        }
        //如果上面的循环结束但是找不到合适的矩形，这里有个大问题：如果index快遍历完了，剩下的举行里面没有满足
        // 但是我们会依然把index的矩形换到scoreIndex=0的矩形
        //交换矩形
        MyRectangle temp = new MyRectangle(rectangleTable.get(scoreIndex));
        rectangleTable.set(scoreIndex, rectangleTable.get(index));
        rectangleTable.set(index, temp);
        //此时当前下标就是能找到的第一个最高评分矩形的下标
    }

}

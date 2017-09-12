import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image {
    private int height;
    private int width;
    private String path;
    private int[][] r;
    private int[][] g;
    private int[][] b;
    private int[][] grayScale;

    public Image(){}
    public Image(String path) {
        this.path = path;
        readImageFromFile();
        grayScaleImageFilter();
    }

    private void readImageFromFile() {
        BufferedImage img = null;
        try{
            img = ImageIO.read(new File(path));
            height = img.getHeight();
            width = img.getWidth();
            r = new int[width][height];
            g = new int[width][height];
            b = new int[width][height];
            grayScale = new int[width][height];

            int[] aux;

            for(int i = 0; i < height; i++)
                for(int j = 0; j < width; j++)
                {
                    aux = img.getRaster().getPixel(j, i, new int[3]);
                    r[j][i] = aux[0];
                    g[j][i] = aux[1];
                    b[j][i] = aux[2];
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportImageToFile(String fileName)
    {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++)
            {
                int[] aux = new int[3];
                aux[0] = r[j][i];
                aux[1] = g[j][i];
                aux[2] = b[j][i];
                img.getRaster().setPixel(j, i, aux);
            }
        File outputfile = new File(fileName);
        try {
            ImageIO.write(img, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void grayScaleImageFilter()
    {
        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++) {
                int sum = (int)(r[j][i] * 0.299 + g[j][i] * 0.587 + b[j][i] * 0.114);
                //int sum = (int)((r[j][i] + g[j][i] + b[j][i])/3);
                if(sum > 255 )
                    grayScale[j][i] = 255;
                else
                    grayScale[j][i] = sum;
            }
    }

    public void exportGrayScaleImageToFile(String fileName)
    {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++)
            {
                int[] aux = new int[3];
                aux[0] = grayScale[j][i];
                aux[1] = grayScale[j][i];
                aux[2] = grayScale[j][i];
                img.getRaster().setPixel(j, i, aux);
            }
        File outputfile = new File(fileName);
        try {
            ImageIO.write(img, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

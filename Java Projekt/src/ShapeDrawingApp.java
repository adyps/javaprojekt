import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ShapeDrawingApp extends JFrame {
    private List<ShapeEntity> shapes;
    private ShapeDrawingPanel drawingPanel;
    private Circle cup, cupholder, cupbottomview;
    private Rectangle cupbottom, rect;
    private Arc cuphandle;

    public ShapeDrawingApp() {
        shapes = new ArrayList<>();

        int defWidth = 100;
        int defHeight = 100;
        int defDepth = 100;

        /* Létrehozzuk az alakzatokat amik leirják a csészénket
        * 4 alakzatra van bontva, az első maga a csésze, a második a fogója, a harmadik egy tányér alá, a negyedik pedig egy talp a tányérnak */
        cup = new Circle(0, 0, 0, 0, 0, Color.WHITE);
        cuphandle = new Arc(0, 0, 0, 0, 0, Color.DARK_GRAY);
        cupholder = new Circle(0, 0, 0, 0, 0, Color.GRAY);
        cupbottom = new Rectangle(0, 0, 0, 0, defDepth, Color.LIGHT_GRAY);
        cupbottomview = new Circle(0, 0, 0, 0, 0, Color.LIGHT_GRAY);

        /* Az alakzatainkat egy listába tesszük */
        shapes.add(cup);
        shapes.add(cuphandle);
        shapes.add(cupholder);
        shapes.add(cupbottom);
        shapes.add(cupbottomview);

        /* Fontosabb megjelenitendő elemek létrehozása */
        drawingPanel = new ShapeDrawingPanel(shapes, "side");

        JTextField widthField = new JTextField(5);
        JTextField heightField = new JTextField(5);
        JTextField depthField = new JTextField(5);

        JTextField widthField2 = new JTextField(5);
        JTextField heightField2 = new JTextField(5);
        JTextField depthField2 = new JTextField(5);

        JLabel AreaLabel = new JLabel(CalcArea(shapes));
        JLabel VolumeLabel = new JLabel(CalcVolume(shapes));

        JButton changeSizeButton = new JButton("Change size");
        JButton randomizeButton = new JButton("Randomize");
        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");
        JButton deleteButton = new JButton("Delete");
        JButton helpButton = new JButton("?");

        JButton sideViewButton = new JButton("Side View");
        JButton topViewButton = new JButton("Top View");
        JButton underViewButton = new JButton("Under View");

        widthField.setText(defWidth + "");
        heightField.setText(defHeight + "");
        depthField.setText(defDepth + "");
        widthField2.setText(defWidth + "");
        heightField2.setText(defHeight + "");
        depthField2.setText(defDepth + "");

        /* A gombok 'hallgatói' */
        changeSizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    int width = Integer.parseInt(widthField.getText());
                    int height = Integer.parseInt(heightField.getText());
                    int depth = Integer.parseInt(depthField.getText());

                    int width2 = Integer.parseInt(widthField2.getText());
                    int height2 = Integer.parseInt(heightField2.getText());
                    int depth2 = Integer.parseInt(depthField2.getText());

                    /* A bekért adatokat ellenőrizzük, hogy méretarányosak-e, majd újrarajzoljuk a csészénket ezekkel */
                    if (CheckRatios(width, height, depth, width2, height2, depth2)) {
                        SetShapeSize(width, height, depth, width2, height2, depth2, drawingPanel.getView());
                        repaint();
                    } else {
                        JOptionPane.showMessageDialog(null, "The dimensions are not correct");
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid numbers");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        });

        randomizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Random random = new Random();

                double randomWidth = random.nextInt(200) + 50;
                double randomHeight = random.nextDouble(randomWidth*1.5) + randomWidth*0.5;
                double randomDepth = random.nextDouble(randomWidth*1.5) + randomWidth*0.5;

                double randomWidth2 = random.nextDouble(randomWidth*0.5) + randomWidth*1.5;
                double randomHeight2 = random.nextDouble(randomWidth2*0.5) + randomWidth2*1.5;
                double randomDepth2 = random.nextDouble(randomWidth*0.5) + randomWidth2*1.5;

                /* A véletlenszerű generálásnál biztositva vannak a méretarányok ezért nem hivjuk meg a CheckRatios függyvényt
                * generálás után a méreteket beállitjuk a csészénknek majd újrarajzoljuk */
                SetShapeSize((int) randomWidth,(int) randomHeight,(int) randomDepth,(int) randomWidth2,(int) randomHeight2,(int) randomDepth2, drawingPanel.getView());
                widthField.setText((int) randomWidth + "");
                heightField.setText((int) randomHeight + "");
                depthField.setText((int) randomDepth + "");

                widthField2.setText((int) randomWidth2 + "");
                heightField2.setText((int) randomHeight2 + "");
                depthField2.setText((int) randomDepth2 + "");

                AreaLabel.setText(CalcArea(shapes));
                VolumeLabel.setText(CalcVolume(shapes));

                repaint();
            }
        });
        /* Jelenleg a paramétereket mentjük ki, jobb lenne magukat az objektumokat kimenteni, de technikai okok miatt (nem megy) igy csináljuk */
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showSaveDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String[] sizes = {widthField.getText(), heightField.getText(), depthField.getText(), widthField2.getText(), heightField2.getText(), depthField2.getText()};
                    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(selectedFile))) {
                        oos.writeObject(sizes);
                        JOptionPane.showMessageDialog(null, "Shapes saved successfully");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error saving shapes: " + ex.getMessage());
                    }
                }
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(selectedFile))) {
                        String[] sizes = (String[]) ois.readObject();

                        int width = Integer.parseInt(sizes[0]);
                        int height = Integer.parseInt(sizes[1]);
                        int depth = Integer.parseInt(sizes[2]);
                        int width2 = Integer.parseInt(sizes[3]);
                        int height2 = Integer.parseInt(sizes[4]);
                        int depth2 = Integer.parseInt(sizes[5]);

                        SetShapeSize(width, height, depth, width2, height2, depth2, drawingPanel.getView());

                        widthField.setText(width + "");
                        heightField.setText(height + "");
                        depthField.setText(depth + "");
                        widthField2.setText(width2 + "");
                        heightField2.setText(height2 + "");
                        depthField2.setText(depth2 + "");

                        AreaLabel.setText(CalcArea(shapes));
                        VolumeLabel.setText(CalcVolume(shapes));

                        repaint();
                        JOptionPane.showMessageDialog(null, "Shapes loaded successfully");
                    } catch (IOException | ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(null, "Error loading shapes: " + ex.getMessage());
                    }
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " + selectedFile.getName() + "?",
                            "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                    if (response == JOptionPane.YES_OPTION) {
                        selectedFile.delete();
                        JOptionPane.showMessageDialog(null, "Deleted successfully.");
                    }
                }
            }
        });

        sideViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.setView("side");

                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());
                int depth = Integer.parseInt(depthField.getText());

                int width2 = Integer.parseInt(widthField2.getText());
                int height2 = Integer.parseInt(heightField2.getText());
                int depth2 = Integer.parseInt(depthField2.getText());

                SetShapeSize(width, height, depth, width2, height2, depth2, "side");
                repaint();
            }
        });

        topViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.setView("top");

                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());
                int depth = Integer.parseInt(depthField.getText());

                int width2 = Integer.parseInt(widthField2.getText());
                int height2 = Integer.parseInt(heightField2.getText());
                int depth2 = Integer.parseInt(depthField2.getText());

                SetShapeSize(width, height, depth, width2, height2, depth2, "top");
                repaint();
            }
        });

        underViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.setView("under");

                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());
                int depth = Integer.parseInt(depthField.getText());

                int width2 = Integer.parseInt(widthField2.getText());
                int height2 = Integer.parseInt(heightField2.getText());
                int depth2 = Integer.parseInt(depthField2.getText());

                SetShapeSize(width, height, depth, width2, height2, depth2, "under");
                repaint();
            }
        });

        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HelpWindow();
            }
        });

        /* Megjelenitéshez részekre bontjuk a mezőinket */
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Top part:"));
        inputPanel.add(new JLabel("Width:"));
        inputPanel.add(widthField);
        inputPanel.add(new JLabel("Height:"));
        inputPanel.add(heightField);
        inputPanel.add(new JLabel("Depth:"));
        inputPanel.add(depthField);

        JPanel inputPanel2 = new JPanel();
        inputPanel2.add(new JLabel("Bottom part:"));
        inputPanel2.add(new JLabel("Width:"));
        inputPanel2.add(widthField2);
        inputPanel2.add(new JLabel("Height:"));
        inputPanel2.add(heightField2);
        inputPanel2.add(new JLabel("Depth:"));
        inputPanel2.add(depthField2);


        JPanel buttonPanel = new JPanel();
        buttonPanel.add(randomizeButton);
        buttonPanel.add(changeSizeButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(helpButton);

        JPanel viewButtonPanel = new JPanel();
        viewButtonPanel.add(new JLabel("View:"));
        viewButtonPanel.add(sideViewButton);
        viewButtonPanel.add(topViewButton);
        viewButtonPanel.add(underViewButton);

        JPanel sizeDisplayPanel = new JPanel();
        sizeDisplayPanel.add(new JLabel("Area:"));
        sizeDisplayPanel.add(AreaLabel);
        sizeDisplayPanel.add(new JLabel("Volume:"));
        sizeDisplayPanel.add(VolumeLabel);

        /* A szebb megjelenités érdekében (meg azért mert grid layout) egy fő menü panelhez csatoljuk a kisebb paneleket */
        JPanel menuPanel = new JPanel(new GridLayout(5, 1));
        menuPanel.add(buttonPanel);
        menuPanel.add(viewButtonPanel);
        menuPanel.add(inputPanel);
        menuPanel.add(inputPanel2);
        menuPanel.add(sizeDisplayPanel);


        this.setLayout(new BorderLayout(3, 1));
        this.add(menuPanel, BorderLayout.NORTH);
        this.add(drawingPanel, BorderLayout.CENTER);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 1000);
        this.setVisible(true);

    }

    /* A függyvény megvizsgálja, hogy méretarányosak-e a felhasználó által adott adatok
    * gyakorlatban: az eredeti width értékhez viszonyitjuk a többi értéket, és megnézzük, hogy fél és kétszerese között van-e az érték
    * a width2 értéket is viszonyitjuk az eredetihez, de a height2-t és depth2-t már a width2-höz hasonlitjuk */
    public boolean CheckRatios(int width, int height, int depth, int width2, int height2, int depth2) {
        boolean check1 = (((width*2 >= height)  && (width*0.5 <= height)) && ((width*2 >= depth) && (width*0.5 <= depth)));
        boolean check2 = (((width2*2 >= height2) && (width2*0.5 <= height2)) && ((width2*2 >= depth2) && (width2*0.5 <= depth2)));
        boolean check3 = (width*2 >= width2 && width*0.5 <= width2);

        return check1 && check2 && check3;
    }

    /* A függyvény beállitja az alakzat objektumaink értékeit, az x, y koordinátákat a méretek és nézet alapján számolja ki */
    public void SetShapeSize(int width, int height, int depth, int width2, int height2, int depth2, String view) {
        cup.setWidth(width);
        cup.setHeight(height);
        cup.setDepth(depth);
        cup.setX(200);
        cup.setY(200);

        cuphandle.setWidth(width / 4);
        cuphandle.setHeight(height / 6);
        cuphandle.setDepth(width / 50);

        cupholder.setWidth(width2);
        cupholder.setHeight(height2 / 2);
        cupholder.setDepth(depth2);

        cupbottom.setWidth(width2 / 2);
        cupbottom.setHeight(height2 / 8);
        cupbottom.setDepth(depth2 / 2);

        if (Objects.equals(view, "side")) {
            cuphandle.setX(200 + width - width / 8);
            cuphandle.setY(200 + height / 2);
            cupholder.setX(200 - width2 / 4 + width / 16);
            cupholder.setY(200 + height / 2 - height2 / 6);
            cupbottom.setX(cupholder.x + width2 / 4);
            cupbottom.setY(cupholder.y + height2 / 2);

        } else if (Objects.equals(view, "top")) {
            cuphandle.setX(200 + width);
            cuphandle.setY(200 + depth / 2);
            cupholder.setX(200 - width / 2);
            cupholder.setY(200 - depth / 2);

        } else if (Objects.equals(view, "under")) {
            cupholder.setX(200);
            cupholder.setY(200);
            cupbottomview.setX(200 + width2 / 4);
            cupbottomview.setY(200 + depth2 / 4);
            cupbottomview.setWidth(width2 / 2);
            cupbottomview.setHeight(height2 / 8);
            cupbottomview.setDepth(depth2 / 2);
        }

    }

    /* Az alakzatok felszineinek az összegét adja vissza, de mivel csak az adat megjelenitéséhez használjuk stringként */
    public String CalcArea(List<ShapeEntity> shapes) {
        double Area = 0;
        for (ShapeEntity shape : shapes) {
            Area += shape.calculateArea();
        }
        return Integer.toString((int) Area);
    }

    /* Az alakzatok térfogatának az összegét adja vissza, de mivel csak az adat megjelenitéséhez használjuk stringként */
    public String CalcVolume(List<ShapeEntity> shapes) {
        double Volume = 0;
        for (ShapeEntity shape : shapes) {
            Volume += shape.calculateVolume();
        }
        return Integer.toString((int) Volume);
    }

    public static void main(String[] args) {
        new ShapeDrawingApp();
    }
}

class ShapeDrawingPanel extends JPanel {
    private List<ShapeEntity> shapes;
    private String view;

    public ShapeDrawingPanel(List<ShapeEntity> shapes, String view) {
        this.shapes = shapes;
        this.view = view;
    }

    /* technikai okok miatt nem használjuk magyarázat: load és save kommentjeinél */
    public void setShapes(List<ShapeEntity> shapes) {
        this.shapes = shapes;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getView(){
        return view;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        /* Választott nézet alapján rajzoljuk le a csészét, az oldalnézethez mindegyik alakzat kell
        * felülnézetben már a legalsó részt nem látjuk
        * alulnézetben pedig a csészét és a fogóját nem látjuk */
        if (Objects.equals(view, "side")) {
            shapes.get(0).draw(g);
            shapes.get(1).draw(g);
            shapes.get(2).draw(g);
            shapes.get(3).draw(g);

        } else if (Objects.equals(view, "top")) {
            shapes.get(2).drawTop(g);
            shapes.get(1).drawTop(g);
            shapes.get(0).drawTop(g);

        } else if (Objects.equals(view, "under")) {
            shapes.get(2).drawUnder(g);
            shapes.get(4).drawUnder(g);

        }
    }
}
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sudoku {

     class Tile extends JButton {
        int r;
        int c;
        Tile(int r, int c){
            this.r = r;
            this.c = c;
        } 
    }
    int boardWidth = 600;
    int boardHeight = 650;

    String[] puzzle = {
        "--74916-5",
        "2---6-3-9",
        "-----7-1-",
        "-586----4",
        "--3----9-",
        "--62--187",
        "9-4-7---2",
        "67-83----",
        "81--45---"
    };

    String[] solution = {
        "387491625",
        "241568379",
        "569327418",
        "758619234",
        "123784596",
        "496253187",
        "934176852",
        "675832941",
        "812945763"
    };

    JFrame frame = new JFrame("Sudoku");
    JLabel txtLabel = new JLabel();
    JPanel txtPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel buttoPanel = new JPanel();

    JButton numSelected = null;
    int errors = 0;


    Sudoku(){
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        txtLabel.setFont(new Font("Arial", Font.BOLD, 30));
        txtLabel.setHorizontalAlignment(JLabel.CENTER);
        txtLabel.setText("Sudoku : 0");

        txtPanel.add(txtLabel);
        frame.add(txtPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(9,9));
        setupTiles();
        frame.add(boardPanel, BorderLayout.CENTER);
        buttoPanel.setLayout(new GridLayout(1,9));
        setupButton();
        frame.add(buttoPanel,BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    void setupTiles(){
        for(int r = 0; r < 9; r++){
            for(int c = 0; c < 9; c++){
                Tile tile = new Tile(r, c);
                char tileChar = puzzle[r].charAt(c);
                if (tileChar != '-'){
                    tile.setFont(new Font("Arial", Font.BOLD,20));
                    tile.setText(String.valueOf(tileChar));
                    tile.setBackground(Color.lightGray);   
                }else{
                    tile.setFont(new Font("Arial", Font.PLAIN,20));
                    tile.setBackground(Color.white);
                }
                tile.setOpaque(true);  
                
                int top = (r == 0) ? 4 : ((r % 3 == 0) ? 3 : 1);
                int left = (c == 0) ? 4 : ((c % 3 == 0) ? 3 : 1);
                int bottom = (r == 8) ? 4 : 1;
                int right = (c == 8) ? 4 : 1;

                tile.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));

                tile.setFocusable(false);
                boardPanel.add(tile);

                tile.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e){
                        Tile tile = (Tile) e.getSource();
                        int r = tile.r;
                        int c = tile.c;
                        if(numSelected != null){
                            if (tile.getText() != "") {
                                return;
                            }

                            String numSelectedText = numSelected.getText();
                            String tileSolution = String.valueOf(solution[r].charAt(c));
                            if(tileSolution.equals(numSelectedText)){
                                tile.setText(numSelectedText);
                            }else{
                                errors +=1;
                                txtLabel.setText(("Mistake: " + String.valueOf(errors)));
                            }

                        }
                    }
                });
            }
        }
        
    }

    void setupButton(){
        for(int i = 1; i<10; i++){
            JButton button = new JButton();
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setText(String.valueOf(i));
            button.setFocusable(false);
            button.setBackground(Color.white);
            buttoPanel.add(button);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    JButton button = (JButton) e.getSource();
                    if (numSelected != null) {
                        numSelected.setBackground(Color.white);
                    }
                    numSelected = button;
                    numSelected.setBackground(Color.lightGray);
                }
            });
        }
    }
}

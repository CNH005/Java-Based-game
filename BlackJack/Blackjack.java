import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Blackjack {
    private class  Card {
        String value, type;

        Card(String value, String type){
            this.value = value;
            this.type = type;
        }
        
        public String toString(){
            return value + "-" + type;
        }


        public int getValue(){
            if("AJQK".contains(value)){
                if(value == "A"){
                    return 11;
                }
                return 10;
            
            }
            return Integer.parseInt(value); // for 2 to 10
        }

        public boolean isAce(){
            return value == "A";
        }
        public String getImagePath(){
            return "./cards/" + toString() + ".png";
        }

    }

    ArrayList<Card> deck;
    Random random = new Random(); //shuffel deck;

    //for dealer
    Card hiddenCard;
    ArrayList<Card> dealerHand;
    int dealerSum;
    int dealerAceCount;

    //player
    ArrayList<Card> playerHand;
    int playerSum;
    int playerAceCount;

    //for window
        int boardwidth = 600;
        int boardheight = boardwidth;

        int cardWidth = 110;
        int cardHeight = 154;



        JFrame frame = new JFrame("Black Jack");
        JPanel gamePanel = new JPanel(){
            @Override
            public void paintComponent (Graphics g){
                super.paintComponent(g);
                
                try {
                    //draw hidden card
                Image hiddencardImage = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
                if(!stayButton.isEnabled()){
                    hiddencardImage = new ImageIcon(getClass().getResource(hiddenCard.getImagePath())).getImage();
                }

                g.drawImage(hiddencardImage, 20, 20, cardWidth, cardHeight, null);
                   
                //draw dealer hand
                for (int i = 0; i<dealerHand.size(); i++){
                    Card card = dealerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg, cardWidth+ 25 + (cardWidth + 5)*i, 20,cardWidth, cardHeight , null);
                }

                //draw player's hand
                for(int i=0 ; i<playerHand.size(); i++){
                    Card card = playerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg, 20 + (cardWidth + 5)*i, 320,cardWidth, cardHeight , null);
                }
                if (!stayButton.isEnabled()) {
                    dealerSum = reduceDealerAce();
                    playerSum = reducePlayerAce();
                    System.out.println("Stay : ");
                    System.out.println(dealerSum);
                    System.out.println(playerSum);

                    String message = "";
                    if (playerSum > 21) {
                        message = "You lose!";
                    }else if(dealerSum >21){
                        message = "You win!";
                    }else if (playerSum == dealerSum){
                        message = "Tie!";
                    }else if(playerSum > dealerSum){
                        message = "You win!";
                    }else if (dealerSum > playerSum){
                        message = "You lose!";
                    }

                    g.setFont(new Font("Arial" , Font.PLAIN, 30));
                    g.setColor(Color.white);
                    g.drawString(message, 220, 250);
                }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                 
                
            }
        };
        JPanel buttonPanel = new JPanel();
        JButton hitButton = new JButton("Hit");
        JButton stayButton = new JButton("Stay");

        
        Blackjack(){
            startGame();

            frame.setVisible(true);
            frame.setSize(boardwidth,boardheight);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            gamePanel.setLayout(new BorderLayout());
            gamePanel.setBackground(new Color(53,101,77));
            frame.add(gamePanel);

            hitButton.setFocusable(false);
            buttonPanel.add(hitButton);
            stayButton.setFocusable(false);
            buttonPanel.add(stayButton);
            frame.add(buttonPanel,BorderLayout.SOUTH);

            hitButton.addActionListener(new ActionListener() {
                public void actionPerformed (ActionEvent e){
                    Card card = deck.remove(deck.size()-1);
                    playerSum += card.getValue();
                    playerAceCount += card.isAce() ? 1 : 0;
                    playerHand.add(card);
                    if (reducePlayerAce() >21 ) {
                        hitButton.setEnabled(false);  // make hit button unclickable after 21
                    }

                    gamePanel.repaint();
                }
            });


            stayButton.addActionListener(new ActionListener() {
                public void actionPerformed (ActionEvent e){
                    hitButton.setEnabled(false);
                    stayButton.setEnabled(false);

                    while (dealerSum <17) {
                        Card card = deck.remove(deck.size()-1);
                        dealerSum += card.getValue();
                        dealerAceCount += card.isAce()? 1: 0;
                        dealerHand.add(card);
                    }
                    gamePanel.repaint();
                }
            });

            gamePanel.repaint();
        }

    public  void startGame(){
        //deck
        buildDeck();
        shuffelDeck();


        //dealer
        dealerHand = new ArrayList<Card>();
        dealerSum = 0;
        dealerAceCount =0;
        
        hiddenCard = deck.remove(deck.size()-1);
        dealerSum += hiddenCard.getValue();
        dealerAceCount += hiddenCard.isAce() ? 1 : 0;


        Card card = deck.remove(deck.size()-1);
        dealerSum += card.getValue();
        dealerAceCount += card.isAce()? 1 : 0;
        dealerHand.add(card);

        System.out.println("Dealer: ");
        System.out.println(hiddenCard);
        System.out.println(dealerHand);
        System.out.println(dealerSum);
        System.out.println(dealerAceCount);

    

        //player
        playerHand = new ArrayList<Card>();
        playerSum = 0;
        playerAceCount = 0;

        for(int i =0; i<2; i++){
            card = deck.remove(deck.size() -1);
            playerSum += card.getValue();
            playerAceCount += card.isAce() ? 1 : 0;
            playerHand.add(card);

        }
        System.out.println("Player : ");
        System.out.println(playerHand);
        System.out.println(playerSum);
        System.out.println(playerAceCount);

    }

    public void buildDeck(){
        deck = new ArrayList<Card>();
        String[] values = {"A", "2" , "3", "4" , "5", "6" , "7", "8" , "9", "10" , "J", "Q" , "K"};
        String[] types = {"C", "D" , "H", "S"};


        for(int i =0 ; i<types.length; i++){
            for(int j=0; j<values.length; j++){
                Card card = new Card(values[j],types[i]);
                deck.add(card);
            }
        }
            System.out.println("Build deck");
            System.out.println(deck);
    }

    public void  shuffelDeck(){
        for(int i = 0; i< deck.size(); i++){
            int j = random.nextInt(deck.size());
            Card currCard = deck.get(i);
            Card randomCard = deck.get(j);
            deck.set(i, randomCard);
            deck.set(j, currCard);
        }
        System.out.println("After shuffle");
        System.out.println(deck);
    }

    public int reducePlayerAce(){
        while (playerSum > 21 && playerAceCount > 0 ) {
            playerSum -=10;
            playerAceCount -=1;
        }
        return playerSum;
    }

    public int reduceDealerAce(){
        while (dealerSum > 21 && dealerAceCount >0 ) {
            dealerSum -=10;
            dealerAceCount -= 1;  
        }
        return dealerSum;
    }

}

package lab6;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;

/*
I made two AIs adapting two different strategies: AI 1 prefers to defeat cards and AI 2 prefers to score
My conclusion is that AI 2 has a stronger tendency to win.
 */

public class Lab6 extends Application {
    static final int INITIAL_HAND_NUMBER = 3;
    static final int SCORE_TO_WIN = 50;
    static Player player1;
    static Player player2;

    public HandBox player1HandBox;
    public DeckBox player1DeckBox;
    public HandBox player2HandBox;
    public DeckBox player2DeckBox;

    public static void main(String[] args) {
        launch(args);
//        game();
    }

    @Override
    public void start(Stage primaryStage){

//        player1 = new AIPlayer("AIPlayer1");
//        player2 = new AIPlayer("AIPlayer2");
//
//        player1.setOpponent(player2);
//        player2.setOpponent(player1);
//
//        shuffleCards(player1, player2);
//        player1.showDeck();
//        player2.showDeck();
//
//        for(int i = 0;i<INITIAL_HAND_NUMBER;i++){
//            player1.drawACard();
//            player2.drawACard();
//        }

        MyPane pane = new MyPane();
//        GridPane pane = new GridPane();
//        player1HandBox = new HandBox(player1);
//        pane.add(player1HandBox,1,0);
//
//        player1DeckBox = new DeckBox("player1");
//        pane.add(player1DeckBox,0,1);
//
//        pane.add(new BoardBox(),1,1);
//
//        player2DeckBox = new DeckBox("player2");
//        pane.add(player2DeckBox,2,1);
//
//        player2HandBox = new HandBox(player2);
//        pane.add(player2HandBox,1,2);

//        pane.setAlignment(Pos.CENTER);
//        pane.setGridLinesVisible(true);
        Scene scene = new Scene(pane, 700,800);
        primaryStage.setTitle("Lab6");
        primaryStage.setScene(scene);
        primaryStage.show();


//        Timeline animation = new Timeline(
//                new KeyFrame(Duration.millis(5000), e -> pane.game()));
//        animation.setCycleCount(Timeline.INDEFINITE);
//        animation.play();




    }





    public static void printBoard(){
        System.out.println(player1.score + "                Player 1");
        System.out.println("Hand1 "+Arrays.toString(player1.hand.toArray()));
        System.out.println("Field1 " + Arrays.toString(player1.field.toArray()));
        System.out.println("Field2 " + Arrays.toString(player2.field.toArray()));
        System.out.println("Hand2 " + Arrays.toString(player2.hand.toArray()));
        System.out.println(player2.score + "                Player 2");
    }

    public static ArrayList<Card> generateDeck() {
        ArrayList<Card> deck = new ArrayList<Card>();
        for (int i = 1; i <= 13; i++) {
            deck.add(new Card(Suit.Club, i));
            deck.add(new Card(Suit.Diamond, i));
            deck.add(new Card(Suit.Heart, i));
            deck.add(new Card(Suit.Spade, i));
        }
        return deck;
    }

    public static void shuffleCards(Player player1, Player player2) {
        ArrayList<Card> deck = generateDeck();
        Collections.shuffle(deck);
        for (int i = 0; i < deck.size() - 1; i += 2) {
            player1.deck.add(deck.get(i));
            player2.deck.add(deck.get(i + 1));
        }
    }
}
enum Suit {
    Club, Diamond, Heart, Spade
}

class MyPane extends GridPane {
    public HandBox player1HandBox;
    public DeckBox player1DeckBox;
    public HandBox player2HandBox;
    public DeckBox player2DeckBox;
    public BoardBox boardBox;
    public Player player1;
    public Player player2;
    final int INITIAL_HAND_NUMBER = 3;
    final int SCORE_TO_WIN = 50;
    Button phaseButton = new Button("Phase 1");
    Button restartButton = new Button("Restart");
    Text infoField = new Text();
    int count = 0;

    public MyPane(){

        this.player1 = new AIPlayer("AIPlayer1", StrategyPreference.defeatOpponentCards);
        this.player2 = new AIPlayer("AIPlayer2", StrategyPreference.gainScoresQuickly);

        player1.setOpponent(player2);
        player2.setOpponent(player1);

        player1HandBox = new HandBox(player1);
        this.add(player1HandBox,1,0);

        player1DeckBox = new DeckBox(player1);
        this.add(player1DeckBox,0,1);

        boardBox = new BoardBox(player1,player2);
        this.add(boardBox,1,1);

        player2DeckBox = new DeckBox(player2);
        this.add(player2DeckBox,2,1);

        player2HandBox = new HandBox(player2);
        this.add(player2HandBox,1,2);

        Dialog<String> dialog = new Dialog<String>();
        dialog.setTitle("Rules");
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        String rulesText = "This is a game played by two AIs.\n" +
                "Core Mechanism:\n\t1. You can remove a card on your opponent's field when you play an Ace.\n" +
                "\t2. You have to remove a card on your field when you play a Jack, Queen, or King.\n" +
                "\t3. Each player has a maximum of 5 cards on hand and on field.\n" +
                "\t4. The first player who gains 50 points wins this game.\n" +
                "\nA typical game goes through the following steps:\n" +
                "\tPhase 1: Each player either draw a card or play a card\n" +
                "\tPhase 2: Each player decide the action of their cards on field." +
                " Each card can either score(according to their current strength) or attack " +
                "one card on the opponent's field\n" +
                "\t* Each phase is carried out blindly, which means the player won't know what their opponent" +
                " plays until it's been done.\n" +
                "\t* The battle phase is carried out such that the result meets each player's intention. " +
                "For every additional card that attacks the same card, the card being attacked " +
                "gains 50% extra strength.";


        dialog.setContentText(rulesText);
        dialog.getDialogPane().getButtonTypes().add(type);

        //Creating a button
        Button rulesButton = new Button("RULES");
        //Showing the dialog on clicking the button
        rulesButton.setOnAction(e -> {
            dialog.showAndWait();
        });
        VBox dialogBox = new VBox();
        dialogBox.getChildren().add(rulesButton);
        dialogBox.setAlignment(Pos.CENTER);
        this.add(dialogBox,2,0);

        VBox player1LabelBox = new VBox();
        Text player1Label = new Text(player1.name);
        player1LabelBox.getChildren().add(player1Label);
        player1LabelBox.setAlignment(Pos.CENTER);
        this.add(player1LabelBox,0,0);

        VBox player2LabelBox = new VBox();
        Text player2Label = new Text(player2.name);
        player2LabelBox.getChildren().add(player2Label);
        player2LabelBox.setAlignment(Pos.CENTER);
        this.add(player2LabelBox,0,2);

        VBox infoLabelBox = new VBox();
        Text infoLabel = new Text("Next phase actions");
        infoLabelBox.getChildren().add(infoLabel);
        infoLabelBox.setAlignment(Pos.CENTER);
        this.add(infoLabelBox,0,3);

        VBox textFieldBox = new VBox();
        textFieldBox.setPrefSize(500,150);

        infoField.setText(getPhase1Info());
        infoField.setTextAlignment(TextAlignment.CENTER);
        textFieldBox.getChildren().add(infoField);
        textFieldBox.setAlignment(Pos.CENTER);
        this.add(textFieldBox, 1,3);

        HBox buttonBox = new HBox();


        restartButton.setDisable(true);
        buttonBox.getChildren().addAll(phaseButton,restartButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(20);
        this.add(buttonBox,1,4);


        initialize();
        phaseButton.setOnAction(e -> {
            if(player1.score < SCORE_TO_WIN && player2.score < SCORE_TO_WIN){
                if(count % 2 == 0){
                    phaseButton.setText("Phase 2");
                    phase1();
                    infoField.setText(getPhase2Info());
                }

                else{
                    phaseButton.setText("Phase 1");
                    phase2();
                    infoField.setText(getPhase1Info());
                }

                count++;
            }
            else{
                String winMessage = "";
                winMessage = player1.score >=50 ? "Player 1 Wins" : "Player 2 Wins";
                infoField.setText(winMessage);
                phaseButton.setDisable(true);
                restartButton.setDisable(false);
            }
        });

        restartButton.setOnAction(e -> {
            initialize();
        });
        this.setAlignment(Pos.CENTER);
        this.setGridLinesVisible(true);
    }



    public void initialize(){
        count = 0;
        phaseButton.setDisable(false);
        player1.reset();
        player2.reset();
        shuffleCards(player1, player2);

        for(int i = 0;i<INITIAL_HAND_NUMBER;i++){
            player1.drawACard();
            player2.drawACard();
        }
        update();
        restartButton.setDisable(true);
    }

    public void update(){
        player1HandBox.update();
        player2HandBox.update();
        boardBox.update();
        player1DeckBox.update();
        player2DeckBox.update();
    }

    public String getPhase1Info(){
        String info = "";
        Action player1PhaseOneAction = player1.decideForPhaseOne();

        info += "Player1 Chooses to " + player1PhaseOneAction.toString();

        Action player2PhaseOneAction = player2.decideForPhaseOne();

        info+="\nPlayer2 Chooses to " + player2PhaseOneAction.toString();
        return info;
    }

    public String getPhase2Info(){
        ArrayList<CardAction> cardActionArrayList = new ArrayList<>();
        String infoText = "\n";
        player1.decideForPhaseTwo(cardActionArrayList);
        player2.decideForPhaseTwo(cardActionArrayList);

        LinkedHashMap<Card,ArrayList<Card>> cardMap = new LinkedHashMap<>();
        HashSet<Card> cardsOnField = new HashSet<>();
        for(CardAction cardAction : cardActionArrayList){
            cardsOnField.add(cardAction.thisCard);
        }
        for(Card c : cardsOnField){
            cardMap.put(c,new ArrayList<>());
        }
        for(CardAction cardAction : cardActionArrayList){
            // score first
            if(cardAction.action == Action.Score){
                infoText += cardAction.thisCard.toString() + " scores\n";
            }
            else if(cardAction.action == Action.Attack){
                infoText += cardAction.thisCard +" attacks " + cardAction.targetCard+"\n";
            }
        }
        System.out.println(infoText);
        return infoText;
    }

    public void phase1(){
        System.out.println("-----------Phase 1-----------");
        //two players first make a decision for Phase 1(draw or play a card)
        Action player1PhaseOneAction = player1.decideForPhaseOne();

        System.out.println("Player1 Chooses to " + player1PhaseOneAction.toString());

        Action player2PhaseOneAction = player2.decideForPhaseOne();
        System.out.println("Player2 Chooses to " + player2PhaseOneAction.toString());

//        infoField.setText("Player1 Chooses to " + player1PhaseOneAction.toString()+"\nPlayer2 Chooses to " + player2PhaseOneAction.toString());
        //execute their decisions
        switch(player1PhaseOneAction){
            case Draw:
                player1.drawACard();
                break;
            case Play:
                player1.playACardOnField();
                break;
        }
        switch(player2PhaseOneAction){
            case Draw:
                player2.drawACard();
                break;
            case Play:
                player2.playACardOnField();
                break;
        }
        update();
    }

    public void phase2(){
        ArrayList<CardAction> cardActionArrayList = new ArrayList<>();
        System.out.println("-----------Phase 2-----------");
        //each player decides the action for each card
        String infoText = "\n";
        player1.decideForPhaseTwo(cardActionArrayList);
        player2.decideForPhaseTwo(cardActionArrayList);



//            Card A = new Card(Suit.Club,9);
//            Card B = new Card(Suit.Club, 4);
//            Card C = new Card(Suit.Club, 5);
//            Card D = new Card(Suit.Club, 7);
//            Card E = new Card(Suit.Club, 3);
//            Card F = new Card(Suit.Club, 6);
//            A.setOwner(player1);
//            B.setOwner(player1);
//            C.setOwner(player1);
//            D.setOwner(player2);
//            E.setOwner(player2);
//            F.setOwner(player2);
//            cardActionArrayList.add(new CardAction(player1,A,D,Action.Attack));
//            cardActionArrayList.add(new CardAction(player1,B,E,Action.Attack));
//            cardActionArrayList.add(new CardAction(player1,C,F,Action.Attack));
//            cardActionArrayList.add(new CardAction(player2,D,A,Action.Attack));
//            cardActionArrayList.add(new CardAction(player2,E,A,Action.Attack));
//            cardActionArrayList.add(new CardAction(player2,F,C,Action.Attack));

        LinkedHashMap<Card,ArrayList<Card>> cardMap = new LinkedHashMap<>();
        HashSet<Card> cardsOnField = new HashSet<>();
        for(CardAction cardAction : cardActionArrayList){
            cardsOnField.add(cardAction.thisCard);
        }
        for(Card c : cardsOnField){
            cardMap.put(c,new ArrayList<>());
        }
        for(CardAction cardAction : cardActionArrayList){
            // score first
            if(cardAction.action == Action.Score){
                infoText += cardAction.thisCard.toString() + " scores\n";
                cardAction.player.score += cardAction.thisCard.currentValue;
            }
            else if(cardAction.action == Action.Attack){
//                    if(!cardMap.containsKey(cardAction.targetCard)){
//                        cardMap.put(cardAction.targetCard,new ArrayList<>(Arrays.asList(cardAction.thisCard)));
//                    }
                infoText += cardAction.thisCard +" attacks " + cardAction.targetCard+"\n";
                cardMap.get(cardAction.targetCard).add(cardAction.thisCard);

            }
        }
//        System.out.println(infoText);
//        infoField.setText(infoText);

        for(Map.Entry<Card,ArrayList<Card>>it:cardMap.entrySet()){
            System.out.println(""+it.getKey() + " " + it.getValue());
        }

        // set temp
        for(Card c : cardsOnField)
            c.tempValue = c.currentValue;



        // add 50% of the value if being a target of more than 1 cards
        for(Card targetCard : cardMap.keySet()){
            if(cardMap.get(targetCard).size() > 1){
                targetCard.tempValue += targetCard.tempValue * 0.5 * (cardMap.get(targetCard).size()-1);
            }
        }
        // deal with two cards attacking each other

        for(Card c : cardsOnField){
            Card cardAlsoAttackingMe = null;
            for(Card attackingCard : cardMap.get(c)){
                if(cardMap.get(attackingCard).contains(c)){
                    cardAlsoAttackingMe = attackingCard;
                    break;
                }

            }
            if(cardAlsoAttackingMe!=null){
                c.tempValue -= c.receiveAttackFrom(cardAlsoAttackingMe);
                cardAlsoAttackingMe.tempValue -= cardAlsoAttackingMe.receiveAttackFrom(c);
                cardMap.get(c).remove(cardAlsoAttackingMe);
                cardMap.get(cardAlsoAttackingMe).remove(c);
            }
        }


        // reduce the value if attacking or being attacked
        for(Card targetCard : cardMap.keySet()){
            for(Card attackingCard : cardMap.get(targetCard)){
                targetCard.tempValue -= targetCard.receiveAttackFrom(attackingCard);
                attackingCard.tempValue -= attackingCard.receiveAttackFrom(targetCard);
            }
        }

        // set currentValue to tempValue
        for(Card c : cardsOnField){
            c.currentValue = c.tempValue;
//                System.out.println(c.toString() + " " + c.originalValue);
        }


        // defeated cards go to opponent's deck
        for(Card c : cardsOnField){
            if(c.currentValue <= 0){
                if(c.owner.equals(player1)){
                    player1.field.remove(c);
                    c.setToDefault();
                    player2.deck.addLast(c);
                    c.owner = null;
                }
                else if(c.owner.equals(player2)){
                    player2.field.remove(c);
                    c.setToDefault();
                    player1.deck.addLast(c);
                    c.owner = null;
                }
            }
        }

        update();
    }

    public static ArrayList<Card> generateDeck() {
        ArrayList<Card> deck = new ArrayList<Card>();
        for (int i = 1; i <= 13; i++) {
            deck.add(new Card(Suit.Club, i));
            deck.add(new Card(Suit.Diamond, i));
            deck.add(new Card(Suit.Heart, i));
            deck.add(new Card(Suit.Spade, i));
        }
        return deck;
    }

    public static void shuffleCards(Player player1, Player player2) {
        ArrayList<Card> deck = generateDeck();
        Collections.shuffle(deck);
        for (int i = 0; i < deck.size() - 1; i += 2) {
            player1.deck.add(deck.get(i));
            player2.deck.add(deck.get(i + 1));
        }
    }



    public void game(){

        update();
        printBoard();


        update();


        printBoard();


        update();
        printBoard();
    }

    public void game1(){
        int turn = 1;
        while(player1.score<SCORE_TO_WIN && player2.score<SCORE_TO_WIN){
            ArrayList<CardAction> cardActionArrayList = new ArrayList<>();
            System.out.println("===========Turn " + turn+"===========");

            printBoard();
            System.out.println("-----------Phase 1-----------");
            //two players first make a decision for Phase 1(draw or play a card)
            Action player1PhaseOneAction = player1.decideForPhaseOne();
            System.out.println("Player1 Chooses to " + player1PhaseOneAction.toString());
            Action player2PhaseOneAction = player2.decideForPhaseOne();
            System.out.println("Player2 Chooses to " + player2PhaseOneAction.toString());

            //execute their decisions
            switch(player1PhaseOneAction){
                case Draw:
                    player1.drawACard();
                    break;
                case Play:
                    player1.playACardOnField();
                    break;
            }
            switch(player2PhaseOneAction){
                case Draw:
                    player2.drawACard();
                    break;
                case Play:
                    player2.playACardOnField();
                    break;
            }

            player1HandBox.update();
            player2HandBox.update();

//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            printBoard();
            System.out.println("-----------Phase 2-----------");
            //each player decides the action for each card

            player1.decideForPhaseTwo(cardActionArrayList);
            player2.decideForPhaseTwo(cardActionArrayList);



//            Card A = new Card(Suit.Club,9);
//            Card B = new Card(Suit.Club, 4);
//            Card C = new Card(Suit.Club, 5);
//            Card D = new Card(Suit.Club, 7);
//            Card E = new Card(Suit.Club, 3);
//            Card F = new Card(Suit.Club, 6);
//            A.setOwner(player1);
//            B.setOwner(player1);
//            C.setOwner(player1);
//            D.setOwner(player2);
//            E.setOwner(player2);
//            F.setOwner(player2);
//            cardActionArrayList.add(new CardAction(player1,A,D,Action.Attack));
//            cardActionArrayList.add(new CardAction(player1,B,E,Action.Attack));
//            cardActionArrayList.add(new CardAction(player1,C,F,Action.Attack));
//            cardActionArrayList.add(new CardAction(player2,D,A,Action.Attack));
//            cardActionArrayList.add(new CardAction(player2,E,A,Action.Attack));
//            cardActionArrayList.add(new CardAction(player2,F,C,Action.Attack));

            LinkedHashMap<Card,ArrayList<Card>> cardMap = new LinkedHashMap<>();
            HashSet<Card> cardsOnField = new HashSet<>();
            for(CardAction cardAction : cardActionArrayList){
                cardsOnField.add(cardAction.thisCard);
            }
            for(Card c : cardsOnField){
                cardMap.put(c,new ArrayList<>());
            }
            for(CardAction cardAction : cardActionArrayList){
                // score first
                if(cardAction.action == Action.Score){
                    cardAction.player.score += cardAction.thisCard.currentValue;
                }
                else if(cardAction.action == Action.Attack){
//                    if(!cardMap.containsKey(cardAction.targetCard)){
//                        cardMap.put(cardAction.targetCard,new ArrayList<>(Arrays.asList(cardAction.thisCard)));
//                    }

                    cardMap.get(cardAction.targetCard).add(cardAction.thisCard);

                }
            }

            for(Map.Entry<Card,ArrayList<Card>>it:cardMap.entrySet()){
                System.out.println(""+it.getKey() + " " + it.getValue());
            }

            // set temp
            for(Card c : cardsOnField)
                c.tempValue = c.currentValue;



            // add 50% of the value if being a target of more than 1 cards
            for(Card targetCard : cardMap.keySet()){
                if(cardMap.get(targetCard).size() > 1){
                    targetCard.tempValue += targetCard.tempValue * 0.5 * (cardMap.get(targetCard).size()-1);
                }
            }
            // deal with two cards attacking each other

            for(Card c : cardsOnField){
                Card cardAlsoAttackingMe = null;
                for(Card attackingCard : cardMap.get(c)){
                    if(cardMap.get(attackingCard).contains(c)){
                        cardAlsoAttackingMe = attackingCard;
                        break;
                    }

                }
                if(cardAlsoAttackingMe!=null){
                    c.tempValue -= c.receiveAttackFrom(cardAlsoAttackingMe);
                    cardAlsoAttackingMe.tempValue -= cardAlsoAttackingMe.receiveAttackFrom(c);
                    cardMap.get(c).remove(cardAlsoAttackingMe);
                    cardMap.get(cardAlsoAttackingMe).remove(c);
                }
            }


            // reduce the value if attacking or being attacked
            for(Card targetCard : cardMap.keySet()){
                for(Card attackingCard : cardMap.get(targetCard)){
                    targetCard.tempValue -= targetCard.receiveAttackFrom(attackingCard);
                    attackingCard.tempValue -= attackingCard.receiveAttackFrom(targetCard);
                }
            }

            // set currentValue to tempValue
            for(Card c : cardsOnField){
                c.currentValue = c.tempValue;
//                System.out.println(c.toString() + " " + c.originalValue);
            }


            // defeated cards go to opponent's deck
            for(Card c : cardsOnField){
                if(c.currentValue <= 0){
                    if(c.owner.equals(player1)){
                        player1.field.remove(c);
                        c.setToDefault();
                        player2.deck.addLast(c);
                        c.owner = null;
                    }
                    else if(c.owner.equals(player2)){
                        player2.field.remove(c);
                        c.setToDefault();
                        player1.deck.addLast(c);
                        c.owner = null;
                    }
                }
            }

            printBoard();
            turn++;
        }
        System.out.println("=====Game Over=====");
        String winner = player1.score >=50 ? "player1" : "player2";
        System.out.println(winner + " wins!");
    }

    public void printBoard(){
        System.out.println(player1.score + "                Player 1");
        System.out.println("Hand1 "+Arrays.toString(player1.hand.toArray()));
        System.out.println("Field1 " + Arrays.toString(player1.field.toArray()));
        System.out.println("Field2 " + Arrays.toString(player2.field.toArray()));
        System.out.println("Hand2 " + Arrays.toString(player2.hand.toArray()));
        System.out.println(player2.score + "                Player 2");
    }
}

interface UIComponents{
    public void update();
}

class DeckBox extends VBox{
    Player player;
    public DeckBox(Player player){
        this.player = player;
        update();
    }

    public void update(){
        getChildren().clear();
        Text playerName = new Text(player.name);
        Text remainingCards = new Text(player.deck.size() + " cards in deck");
        Text scoreText = new Text("Score: "+player.score);
        getChildren().addAll(playerName,remainingCards,scoreText);
        setAlignment(Pos.CENTER);
    }
}

class HandBox extends HBox implements UIComponents{
    Player player;
    public HandBox(Player player){
        this.player = player;
        update();
    }

    @Override
    public void update() {
        this.setSpacing(5);
        this.setPrefSize(500,100);
        this.setPadding(new Insets(15, 15, 15, 15));

        this.getChildren().clear();
        for(Card c : player.hand){
            getChildren().add(c.cardImage);
        }
        setAlignment(Pos.CENTER);
    }
}

class FieldBox extends HBox implements UIComponents{
    Player player;
    public FieldBox(Player player){
        this.player = player;
        update();
    }

    @Override
    public void update() {
        this.setSpacing(5);
        this.setPrefSize(500,100);
        this.setPadding(new Insets(15, 15, 15, 15));
        this.getChildren().clear();
        for(Card c : player.field){
            getChildren().add(new CardBox(c));
        }
        setAlignment(Pos.CENTER);
    }
}

class BoardBox extends VBox implements UIComponents{
    FieldBox player1FieldBox;
    FieldBox player2FieldBox;
    public BoardBox(Player player1, Player player2){

        this.setPrefSize(500,400);
//        this.getChildren().add(new Text("Board"));
        player1FieldBox = new FieldBox(player1);
        player2FieldBox = new FieldBox(player2);
        this.getChildren().add(player1FieldBox);
        this.getChildren().add(player2FieldBox);
        setAlignment(Pos.CENTER);
        update();
    }

    @Override
    public void update() {
        player1FieldBox.update();
        player2FieldBox.update();
    }
}


class CardBox extends VBox implements UIComponents{
    Card card;
    public CardBox(Card card){
        this.card = card;
        update();
    }

    @Override
    public void update() {
        getChildren().clear();
        getChildren().addAll(card.generateImage());
    }
}

class CardImage extends Pane{
    Card card;
    public CardImage(Card card){
        this.card = card;
        if(card.currentValue < card.originalValue)
            updateCard();
        else
            paintCard();
    }

    private String cardSuit(){
        switch(card.suit){
            case Club:
                return "♧️";
            case Spade:
                return "♤️";
            case Diamond:
                return "♢️";
            case Heart:
                return "♡";
        }
        return null;
    }

    private void paintCard(){
        setPrefSize(50,100);
//        setBorder(new Border(new BorderStroke(Color.BLACK,
//                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//        setStyle("-fx-background-color: white");
        Double centerX = getWidth()/2;
        Double centerY = getHeight()/2;
        Double halfWidth = getWidth()*0.95f*0.5f;
        Double halfHeight = getHeight()*0.95f*0.5f;
        Double textStartX = centerX - halfWidth*0.8f;
        Double textStartY = centerY - halfHeight*0.5f;
        Double suitStartX = textStartX*1.05f;
        Double suitStartY = textStartY + halfHeight/2;
        Rectangle cardItSelf = new Rectangle(
                centerX-halfWidth,centerY-halfHeight,halfWidth*2,halfHeight*2);

        String cardValueText = ""+card.currentValue;
        if(card.currentValue == card.originalValue){
            switch (card.currentValue){
                case 11:
                    cardValueText = "J";
                    break;
                case 12:
                    cardValueText = "Q";
                    break;
                case 13:
                    cardValueText = "K";
                    break;
            }

        }
        Text cardValue = new Text(textStartX,textStartY,cardValueText);
        if(card.currentValue < card.originalValue)
            cardValue.setFill(Color.RED);
//        cardValue.setStroke(Color.BLACK);
        cardItSelf.setFill(Color.WHITE);
        cardItSelf.setStroke(Color.BLACK);
        Text suitText = new Text(suitStartX,suitStartY,cardSuit());

        cardItSelf.setArcWidth(10.0);
        cardItSelf.setArcHeight(10.0);

        getChildren().clear();
        getChildren().addAll(cardItSelf,cardValue,suitText);
    }

    @Override
    public void setWidth(double width) {
        super.setWidth(width);
        paintCard();
    }

    @Override
    public void setHeight(double height) {
        super.setHeight(height);
        paintCard();
    }

    private void updateCard(){
        setPrefSize(50,100);
        Double centerX = getWidth()/2;
        Double centerY = getHeight()/2;
        Double halfWidth = getWidth()*0.95f*0.5f;
        Double halfHeight = getHeight()*0.95f*0.5f;
        Double textStartX = centerX - halfWidth*0.8f;
        Double textStartY = centerY - halfHeight*0.5f;
        Double suitStartX = textStartX*1.05f;
        Double suitStartY = textStartY + halfHeight/2;
        Rectangle cardItSelf = new Rectangle(
                centerX-halfWidth,centerY-halfHeight,halfWidth*2,halfHeight*2);

        Text cardValue = new Text(textStartX,textStartY,cardSuit());

        cardValue.setFill(Color.RED);

        cardItSelf.setFill(Color.WHITE);
        cardItSelf.setStroke(Color.BLACK);
        Text suitText = new Text(suitStartX,suitStartY,card.suit.toString());

        cardItSelf.setArcWidth(10.0);
        cardItSelf.setArcHeight(10.0);

        getChildren().clear();
        getChildren().addAll(cardItSelf);
    }
}


class Card implements Comparable<Card>{
    Suit suit;
    int originalValue;
    int currentValue;
    int potentialValue;
    int tempValue;
    Player owner = null;

    CardImage cardImage;

    public CardImage generateImage(){
        return new CardImage(this);
    }

    public Card(Suit suit, int originalValue) {
        this.suit = suit;
        this.originalValue = originalValue;
        this.currentValue = originalValue;
        this.potentialValue = originalValue;
        this.tempValue = originalValue;
        this.cardImage = new CardImage(this);
    }

    public void setOwner(Player player){
        this.owner = player;
    }

    public Card(Card c){
        this.suit = c.suit;
        this.originalValue = c.originalValue;
        this.currentValue = c.currentValue;
        this.potentialValue = c.potentialValue;
    }

    public boolean hasModifier(Card c){
        if(this.suit.equals(Suit.Club)&&c.suit.equals(Suit.Diamond))
            return true;
        else if(this.suit.equals(Suit.Diamond)&&c.suit.equals(Suit.Spade))
            return true;
        else if(this.suit.equals(Suit.Spade)&&c.suit.equals(Suit.Heart))
            return true;
        else if(this.suit.equals(Suit.Heart)&&c.suit.equals(Suit.Club))
            return true;
        else
            return false;
    }

    public String toString() {
        if(this.currentValue!=this.originalValue)
            return suit.toString() + " * " + currentValue;
        else
            return suit.toString() + " " + currentValue;
    }

    @Override
    public int compareTo(Card o) {
        return this.originalValue - o.originalValue;
    }

    public int healthAfterAttack(Card o){
        int thisHealth = this.currentValue;
        int thatHealth = o.currentValue;
        int thisAttack = this.currentValue;
        int thatAttack = o.currentValue;

        if(this.hasModifier(o))
            thisAttack+=4;
        if(o.hasModifier(this))
            thatAttack+=4;

        return thisHealth-thatAttack;
    }

    public int healthAfterAttack(Card targetCard, int targetCardCurrentValue){
        int thisHealth = this.currentValue;
        int thatAttack = targetCardCurrentValue;
        if(targetCard.hasModifier(this))
            thatAttack += 4;
        return thisHealth-thatAttack;
    }

    public int receiveAttackFrom(Card o){
        int attack = o.currentValue;
        if(o.hasModifier(this))
            attack += 4;
        return attack;
    }

    public int receiveAttackFrom(Card attackingCard, int attackingCardCurrentValue){
        int attack = attackingCardCurrentValue;
        if(attackingCard.hasModifier(this))
            attack+=4;
        return attack;
    }

    public boolean isFaceCard(){
        if(this.originalValue == 11||this.originalValue==12||this.originalValue==13)
            return true;
        return false;
    }

    public void setToDefault(){
        this.currentValue = this.originalValue;
        this.tempValue = this.originalValue;
    }
}

enum Action{
    Draw,
    Play,
    Attack,
    Score
}

class CardAction{
    Player player = null;
    Card thisCard = null;
    Card targetCard = null;
    Action action = Action.Score;
    public CardAction(Player player, Card thisCard, Card targetCard, Action action){
        this.player = player;
        this.thisCard = thisCard;
        this.targetCard = targetCard;
        this.action = action;
    }
}

abstract class Player {
    String name;
    LinkedList<Card> deck;
    ArrayList<Card> hand;
    ArrayList<Card> field;
    int score;
    Player opponent;
    public Player(String name) {
        this.name = name;
        this.deck = new LinkedList<>();
        this.hand = new ArrayList<>();
        this.field = new ArrayList<>();
        this.score = 0;
    }

    public void reset(){
        this.field.clear();
        this.hand.clear();
        this.deck.clear();
        this.score = 0;
    }

    public void setOpponent(Player opponent){
        this.opponent = opponent;
    }

    abstract public void drawACard();

    abstract public void playACardOnField();

    abstract public Action decideForPhaseOne();

    abstract public void decideForPhaseTwo(ArrayList<CardAction> cardActionArrayList);

    abstract public void battle();

    abstract public void score();


    public void showDeck() {
        System.out.println(Arrays.toString(this.deck.toArray()));
    }

    public void showHand(){
        System.out.println(Arrays.toString(this.hand.toArray()));
    }

    public void showField(){
        System.out.println(Arrays.toString(this.field.toArray()));
    }

    public void dealWithJQK(Card cardToBeDiscarded){
        if(cardToBeDiscarded!=null){
            this.field.remove(cardToBeDiscarded);
            this.deck.addLast(cardToBeDiscarded);
        }
    }

    public void dealWithAce(Card ace, Card cardRemovedFromOpponent){
        if(cardRemovedFromOpponent!=null){
            opponent.field.remove(cardRemovedFromOpponent);
            this.hand.remove(ace);
            this.deck.addLast(cardRemovedFromOpponent);
            this.deck.addLast(ace);
        }
    }

    public int sumOfStrengthOnField(){
        int strength = 0;
        for(Card c : this.field){
            strength += c.currentValue;
        }
        return strength;
    }

}

class HumanPlayer extends Player {

    public HumanPlayer(String name) {
        super(name);
    }


    public void play() {
        System.out.println("Phase 1: draw a card or play a card on field\nPress 1 to draw, Press 2 to play");
        Scanner scanner = new Scanner(System.in);
        int choice1 = scanner.nextInt();
        switch(choice1){
            case 1:
                drawACard();
                break;
            case 2:
                playACardOnField();
        }
        System.out.println("Phase 2: Battle a card or score a card\nPress 1 to battle, Press 2 to score");
        int choice2 = scanner.nextInt();
        switch(choice2){
            case 1:
                battle();
                break;
            case 2:
                score();
        }
    }

    public void drawACard(){
        if(hand.size() == 5){
            System.out.println("Your hand is full, please choose one card to discard(0-4)");
            int input;
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextInt();
            while(!(input >=0 && input <=4)){
                System.out.println("Invalid input, type again!");
                input = scanner.nextInt();
            }
            deck.addLast(hand.remove(input));
        }
        hand.add(deck.poll());
        showDeck();
        showHand();
    }

    public void playACardOnField(){
        // TODO: special cards effects
        System.out.println("You have " + hand.size() + " cards on hand, " +
                "choose from 0 - " + (hand.size()-1) + " to play a card on the field");
        int input;
        Scanner scanner = new Scanner(System.in);
        input = scanner.nextInt();
        while(!(input >=0 && input <=hand.size()-1)){
            System.out.println("Invalid input, type again!");
            input = scanner.nextInt();
        }
        field.add(hand.remove(input));
    }

    @Override
    public Action decideForPhaseOne() {
        return null;
    }

    @Override
    public void decideForPhaseTwo(ArrayList<CardAction> cardActionArrayList) {
    }

    public void battle(){
        System.out.println("Your opponent has " + opponent.field.size()+" cards on field, "
                + "choose from 0 - " + (opponent.field.size()-1) + " to attack");
        int input1,input2;
        Scanner scanner = new Scanner(System.in);
        input1 = scanner.nextInt();
        Card opponentCard = opponent.field.get(input1);
        System.out.println("You have " + this.field.size()+" cards on field, "+
                "choose from 0 - " + (field.size()-1) + " to attack");
        input2 = scanner.nextInt();
        Card yourCard = this.field.get(input2);

        yourCard.currentValue+=yourCard.healthAfterAttack(opponentCard);
        opponentCard.currentValue-=yourCard.healthAfterAttack(opponentCard);

        if(yourCard.currentValue>0 && opponentCard.currentValue<0){
            opponent.field.remove(opponentCard);
            opponentCard.currentValue = opponentCard.originalValue;
            this.deck.addLast(opponentCard);
        }
        else if(yourCard.currentValue==0&& opponentCard.currentValue==0){
            this.field.remove(yourCard);
            yourCard.currentValue = yourCard.originalValue;
            this.deck.addLast(yourCard);

            opponent.field.remove(opponentCard);
            opponentCard.currentValue = opponentCard.originalValue;
            opponent.deck.addLast(opponentCard);
        }

        else if(yourCard.currentValue<0&&opponentCard.currentValue>0){
            this.field.remove(yourCard);
            yourCard.currentValue = yourCard.originalValue;
            opponent.deck.addLast(yourCard);
        }
    }

    public void score(){
        System.out.println("You have " + field.size() + " cards on hand, " +
                "choose from 0 - " + (field.size()-1) + " to score a card");
        int input;
        Scanner scanner = new Scanner(System.in);
        input = scanner.nextInt();
        while(!(input >=0 && input <=field.size()-1)){
            System.out.println("Invalid input, type again!");
            input = scanner.nextInt();
        }
        this.score+=field.get(input).currentValue;
    }
}

enum StrategyPreference{
    defeatOpponentCards,
    gainScoresQuickly
}

class AIPlayer extends Player {
    StrategyPreference preference;
    public AIPlayer(String name, StrategyPreference preference) {
        super(name);
        this.preference = preference;
    }

    enum Results {
        result0, result1,result2,result3,result4;
    }

    class AttackChoice implements Comparable<AttackChoice>{
        Card cardToAttack;
        Results value;
        public AttackChoice(Card cardToAttack, Card myCard){
            this.cardToAttack = cardToAttack;
            value = returnResult(myCard,cardToAttack);
        }

        @Override
        public int compareTo(AttackChoice o) {
            return value.compareTo(o.value);
//                return value < o.value ? 1 : -1;
        }

        public Results returnResult(Card myCard, Card opponentCard){
            if(myCard.healthAfterAttack(opponentCard)>0)
                return Results.result1;
            else if(myCard.healthAfterAttack(opponentCard) == 0)
                return Results.result2;
            else if(myCard.healthAfterAttack(opponentCard) < 0 && hasCardToDefeatNextTurn(opponentCard,myCard))
                return Results.result3;
            else
                return Results.result4;
        }
    }

    class PhaseOneChoice implements Comparable<PhaseOneChoice>{
        int value;
        Action action;
        final int MAX_VALUE_OF_A_CARD = 13;
        public PhaseOneChoice(Action action){
            this.action = action;
            switch(action){
                // Don't draw a card unless it's necessary
                case Draw:
                    if(hand.size()==0||field.size()==5||(aceIsTheOnlyCardOnHand()&&opponent.field.isEmpty()))
                        this.value=Integer.MAX_VALUE;
                    else if(hand.size()==5)
                        this.value=Integer.MIN_VALUE;
                    else
                        this.value = 2+(5-hand.size());
                    break;
                // Unless your opponent has no cards on the field,
                // attack your opponent's cards first
                case Play:
                    if(field.size() == 5){
                        this.value= Integer.MIN_VALUE;
                    }
                    else if(opponent.field.isEmpty()){
                        // When your opponent has no cards on the field,
                        // the value of a playing a card decreases
                        // as the number of cards on your side's field increases
                        this.value = MAX_VALUE_OF_A_CARD - field.size();
                    }
                    else{
                        // When your opponent has cards on the field,
                        // the value of playing a card is equal to
                        if(sumOfStrengthOnField() > opponent.sumOfStrengthOnField())
                            this.value = MAX_VALUE_OF_A_CARD - field.size();
                        else
                            this.value = 2 * (opponent.sumOfStrengthOnField() - sumOfStrengthOnField());
                    }

            }
        }
        @Override
        public int compareTo(PhaseOneChoice o) {
            return this.value < o.value ? 1 : -1;
        }
    }

    @Override
    public Action decideForPhaseOne(){
        Set<PhaseOneChoice> phaseOneChoices = new HashSet<>();
        Set<Action> options = new HashSet<>(Arrays.asList(Action.values()));
        for(Action action : options){
            phaseOneChoices.add(new PhaseOneChoice(action));
        }
        PriorityQueue<PhaseOneChoice> priorityQueue = new PriorityQueue<>(phaseOneChoices);
        return priorityQueue.poll().action;
    }

    @Override
    public void decideForPhaseTwo(ArrayList<CardAction> cardActionArrayList) {
        for(Card myCard : field){
            Card opponentCard = getBestOpponentCardToAttack(myCard);
            if(opponentCard==null){
                cardActionArrayList.add(new CardAction(this,myCard,null,Action.Score));
            }
            else{
                AttackChoice attackChoice = new AttackChoice(opponentCard,myCard);
                if(this.preference == StrategyPreference.defeatOpponentCards){
                    if(attackChoice.returnResult(myCard,opponentCard) == Results.result4)
                        cardActionArrayList.add(new CardAction(this,myCard,null,Action.Score));
                    else
                        cardActionArrayList.add(new CardAction(this,myCard,opponentCard,Action.Attack));
                }
                else{
                    if(attackChoice.returnResult(myCard,opponentCard) != Results.result0 ||
                            attackChoice.returnResult(myCard,opponentCard) != Results.result1)
                        cardActionArrayList.add(new CardAction(this,myCard,null,Action.Score));
                    else
                        cardActionArrayList.add(new CardAction(this,myCard,opponentCard,Action.Attack));
                }

            }

        }
    }

    @Override
    public void drawACard() {
        if(hand.size()==5){
            int min = Integer.MAX_VALUE;
            Card minCard = null;
            for(Card c : hand){
                if(c.originalValue == 1)
                    continue;
                else{
                    if(c.originalValue < min ){
                        min = c.originalValue;
                        minCard = c;
                    }
                }
            }

            hand.remove(minCard);
        }
        Card drawedCard = deck.removeFirst();
        drawedCard.setOwner(this);
        hand.add(drawedCard);
    }

    @Override
    public void playACardOnField() {
        Card bestCard = getBestCardOnHand();
        hand.remove(bestCard);
        //Ace
        if(bestCard.originalValue == 1){
            int max = 0;
            Card maxCard = null;
            for(Card c : opponent.field){
                if(c.currentValue > max){
                    max = c.currentValue;
                    maxCard = c;
                }
            }
            opponent.field.remove(maxCard);
            if(maxCard!=null)
                maxCard.setToDefault();
            deck.addLast(maxCard);
            deck.addLast(bestCard);
        }
        //JQK
        else if(bestCard.isFaceCard()){
            int min = Integer.MAX_VALUE;
            Card minCard = null;
            for(Card c : field){
                if(c.currentValue<min){
                    min = c.currentValue;
                    minCard = c;
                }
            }
            this.field.remove(minCard);
            if(minCard!=null)
                minCard.setToDefault();
            this.field.add(bestCard);
        }
        else{
            this.field.add(bestCard);
        }
    }

    @Override
    public void battle() {
    }

    @Override
    public void score() {
    }


    public boolean aceIsTheOnlyCardOnHand(){
        if(hand.isEmpty())
            return false;
        else if(hand.size()==1){
            if(hand.get(0).originalValue==1)
                return true;
        }
        return false;
    }

    public Card getBestCardOnHand(){
        if(opponent.field.isEmpty()){
            Card bestCard = null;
            int max = 0;
            Card smallestCard = getSmallestCardOnField();
            int minOnField = smallestCard == null ? 0 : smallestCard.currentValue;
            for(Card c : hand){
                int currValue = 0;
                if(c.originalValue == 11 ||c.originalValue==12||c.originalValue==13)
                    currValue = c.originalValue - minOnField;
                else
                    currValue = c.originalValue;
                if(currValue > max){
                    bestCard = c;
                    max = currValue;
                }
            }
            return bestCard;
        }

        else{
            int maxDiff = Integer.MIN_VALUE;
            Card bestCard = null;
            for(Card c : hand){


                int myFieldStrength = 0,opponentFieldStrength = 0;
                for(Card myCard : this.field)
                    myFieldStrength+=myCard.currentValue;
                for(Card opponentCard : opponent.field)
                    opponentFieldStrength+=opponentCard.currentValue;

                int myCardValue = c.currentValue;
                Card opponentCard = getBestOpponentCardToAttack(c);
                int myOpponentCardValue = opponentCard.currentValue;

                //Ace
                if(c.originalValue == 1){
                    opponentFieldStrength-=myOpponentCardValue;
                    if(myFieldStrength-opponentFieldStrength>maxDiff){
                        bestCard = c;
                        maxDiff = myFieldStrength - opponentFieldStrength;
                    }
                    continue;
                }
                if(c.isFaceCard()){
                    //reduce score on my field
                    Card smallestCard = getSmallestCardOnField();
                    int minOnField = smallestCard == null ? 0 : smallestCard.currentValue;
                    myFieldStrength-=minOnField;

                }
                myFieldStrength += c.healthAfterAttack(opponentCard) >0 ? c.healthAfterAttack(opponentCard) : 0;
                opponentFieldStrength += opponentCard.healthAfterAttack(c) >0 ? opponentCard.healthAfterAttack(c) : 0;

                if(myFieldStrength - opponentFieldStrength > maxDiff){
                    bestCard = c;
                    maxDiff = myFieldStrength - opponentFieldStrength;
                }


            }

            return bestCard;
        }

    }

    public Card getBestOpponentCardToAttack(Card myCard){


        if(myCard.originalValue == 1){
            int max = 0;
            Card bestCard = null;
            for(Card opponentCard : opponent.field){
                if(opponentCard.currentValue>max){
                    max = opponentCard.currentValue;
                    bestCard = opponentCard;
                }
            }
            return bestCard;
        }

        // 1. attack and survive
        // 2. equal strength
        // 3. attack and die, but I have a card that could defeat it next turn
        // 4. attack and die, and I have no cards on hand to defeat it next turn
        Set<AttackChoice> attackChoices = new HashSet<>();

        for(Card opponentCard : opponent.field){
            attackChoices.add(new AttackChoice(opponentCard,myCard));
        }
        PriorityQueue<AttackChoice> priorityQueue = new PriorityQueue<>(attackChoices);

        if(!priorityQueue.isEmpty())
            return priorityQueue.poll().cardToAttack;
        else
             return null;
    }

    public boolean hasCardToDefeatNextTurn(Card opponentCard, Card cardPlayedThisTurn){
        ArrayList<Card> handNextTurn = new ArrayList<>();
        for(Card c : this.hand){
            handNextTurn.add(c);
        }

        handNextTurn.remove(cardPlayedThisTurn);
        Card copyOpponentCard = new Card(opponentCard);
        copyOpponentCard.currentValue-=cardPlayedThisTurn.currentValue;
        for(Card c : handNextTurn){
            if(c.healthAfterAttack(copyOpponentCard)>0)
                return true;
        }
        return false;
    }

    public Card getSmallestCardOnField(){
        int min = Integer.MAX_VALUE;
        Card card = null;
        for(Card c : field){
            if(c.currentValue<min){
                card = c;
                min = c.currentValue;
            }
        }
        return card;
    }
}
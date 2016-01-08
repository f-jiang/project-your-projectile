/**
 * 
 */
package bangersquad.projectile.view.fillintheblanks;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * @author feilan
 *
 */
public class FillInTheBlanks extends AnchorPane {	// TODO: make fill in the blanks work as scene builder "class"

	@FXML
	private TextFlow textFlow;
	
	private String[] prompts = new String[0];

	public FillInTheBlanks() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FillInTheBlanks.fxml"));
		
		loader.setRoot(this);
		loader.setController(this);
		
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
		
/*		getChildren().addListener((ListChangeListener.Change<? extends Node> c) -> {
			while (c.next()) {
				for (Node added : c.getAddedSubList()) {
					if (!(added instanceof Text) && !(added instanceof TextField)) {
						getChildren().remove(added);
					}
				}
			}
		});
*/	}

	/**
	 * 
	 * @param prompts
	 */
	public void setPrompts(String... prompts) {
		this.prompts = prompts;
		applyPrompts();
	}
	
	/**
	 * 
	 * @param textPieces
	 * @param blankRegex
	 */
	public void update(String[] textPieces, String blankRegex) {
		TextField blankText;
		Text nonBlankText;
		ObservableList<Node> children = textFlow.getChildren();
		
		children.clear();

		for (String piece : textPieces) {
			if (piece.matches(blankRegex)) {	// this is a blank
				blankText = new TextField();
				blankText.setPrefWidth(50);	// TODO: calculate TextField width based on font size, max input len
				// input validation: ints only
				// restrict length of input
				children.add(blankText);				
			} else {
				nonBlankText = new Text(piece);
				children.add(nonBlankText);
			}
		}		
		
		applyPrompts();
	}

	/**
	 * 
	 * @param textPieces
	 * @param blankRegex
	 */
	public void update(List<String> textPieces, String blankRegex) {
		TextField blankText;
		Text nonBlankText;
		ObservableList<Node> children = textFlow.getChildren();
		
		children.clear();

		for (String piece : textPieces) {
			if (piece.matches(blankRegex)) {	// this is a blank
				blankText = new TextField();
				blankText.setPrefWidth(50);	// TODO: calculate TextField width based on font size, max input len
				// input validation: ints only
				// restrict length of input
				children.add(blankText);				
			} else {
				nonBlankText = new Text(piece);
				children.add(nonBlankText);
			}
		}
		
		applyPrompts();
	}

	/**
	 * 
	 * @param text
	 * @param blankRegex
	 */
	public void update(String text, String blankRegex) {		
		TextField blankText;
		Text nonBlankText;
		ArrayList<String> textPieces = new ArrayList<>(Arrays.asList(text.split(blankRegex, -1))); // we use this version of split so there can be blank elems at the end
		ObservableList<Node> children = textFlow.getChildren();
		
		children.clear();

		for (int i = 0; i < textPieces.size() - 1; i++) {	
			if (textPieces.get(i).matches("")) {
				continue;
			}

			if (!textPieces.get(i + 1).matches("")) {
				textPieces.add(++i, "");
			}
		}

		for (String piece : textPieces) {
			if (piece.matches("")) {	// this is a blank
				blankText = new TextField();
				blankText.setPrefWidth(50);	// TODO: calculate TextField width based on font size, max input len
				// input validation: ints only
				// restrict length of input
				children.add(blankText);				
			} else {
				nonBlankText = new Text(piece);
				children.add(nonBlankText);
			}
		}
		
		applyPrompts();
	}

	/**
	 * 
	 * @return
	 */
	public String getText() {
		StringBuilder sb = new StringBuilder();

		for (Node child : textFlow.getChildren()) {
			if (child instanceof TextField) {
				sb.append(((TextField) child).getText());
			} else if (child instanceof Text) {
				sb.append(((Text) child).getText());
			}
		}

		return sb.toString();
	}

	private void applyPrompts() {
		int i = 0;
		int l = prompts.length;
		
		for (Node child : textFlow.getChildren()) {
			if (i >= l) {
				break;
			}
			
			if (child instanceof TextField) {
				((TextField) child).setPromptText(prompts[i++]); 
			}
		}
	}
	// TODO: create methods to set text properties such as font, size, colour, etc.

}

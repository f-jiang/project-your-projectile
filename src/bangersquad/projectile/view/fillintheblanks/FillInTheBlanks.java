/**
 * 
 */
package bangersquad.projectile.view.fillintheblanks;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * @author feilan
 *
 */
public class FillInTheBlanks extends TextFlow {

	private String[] prompts = new String[0];
	
	public FillInTheBlanks() {
		getChildren().addListener((ListChangeListener.Change<? extends Node> c) -> {
			while (c.next()) {
				for (Node added : c.getAddedSubList()) {
					if (!(added instanceof Text) && !(added instanceof TextField)) {
						getChildren().remove(added);
					}
				}
			}
		});
	}

	public void setPrompts(String... prompts) {
		this.prompts = prompts;
	}
	
	
	public void update(String[] textPieces, String blankRegex) {
		TextField blankText;
		Text nonBlankText;

		getChildren().clear();

		for (String piece : textPieces) {
			if (piece.matches(blankRegex)) {	// this is a blank
				blankText = new TextField();
				blankText.setPrefWidth(50);	// TODO: calculate TextField width based on font size, max input len
				// input validation: ints only
				// restrict length of input
				getChildren().add(blankText);				
			} else {
				nonBlankText = new Text(piece);
				getChildren().add(nonBlankText);
			}
		}		
		
		applyPrompts();
	}

	public void update(List<String> textPieces, String blankRegex) {
		TextField blankText;
		Text nonBlankText;

		getChildren().clear();

		for (String piece : textPieces) {
			if (piece.matches(blankRegex)) {	// this is a blank
				blankText = new TextField();
				blankText.setPrefWidth(50);	// TODO: calculate TextField width based on font size, max input len
				// input validation: ints only
				// restrict length of input
				getChildren().add(blankText);				
			} else {
				nonBlankText = new Text(piece);
				getChildren().add(nonBlankText);
			}
		}
		
		applyPrompts();
	}

	public void update(String text, String blankRegex) {		
		TextField blankText;
		Text nonBlankText;
		ArrayList<String> textPieces = new ArrayList<>(Arrays.asList(text.split(blankRegex, -1))); // we use this version of split so there can be blank elems at the end

		getChildren().clear();

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
				getChildren().add(blankText);				
			} else {
				nonBlankText = new Text(piece);
				getChildren().add(nonBlankText);
			}
		}
		
		applyPrompts();
	}

	public String getText() {
		StringBuilder sb = new StringBuilder();

		for (Node child : getChildren()) {
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
		
		for (Node child : getChildren()) {
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

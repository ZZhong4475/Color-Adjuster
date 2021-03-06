package contoller;


import static model.PropertyChangeEnabledMutableColor.PROPERTY_GREEN;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import model.ColorModel;
import model.PropertyChangeEnabledMutableColor;

/**
 * Represents a Panel with components used to change and display the Green value for the 
 * backing Color model.
 *
 * @author Charles Bryan
 * @author Zheng Zhong
 * @version Winter 2020
 */
public class GreenRowPanel extends JPanel implements PropertyChangeListener {

    /**  
     * A generated serial version UID for object Serialization. 
     * http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html
     */
    private static final long serialVersionUID = 2284116355218892348L;
    
    /** The size of the increase/decrease buttons. */
    private static final Dimension BUTTON_SIZE = new Dimension(26, 26);
    
    /** The size of the text label. */
    private static final Dimension LABEL_SIZE = new Dimension(45, 26);
    
    /** The number of columns in width of the TextField. */
    private static final int TEXT_FIELD_COLUMNS = 3;
    
    /** The amount of padding for the change panel. */
    private static final int HORIZONTAL_PADDING = 30;
    
    /** The backing model for the system. */
    private final PropertyChangeEnabledMutableColor myColor;

    /** The CheckBox that enables/disables editing of the TextField. */
    private final JCheckBox myEnableEditButton;
    
    /** The TextField that allows the user to type input for the color value. */
    private final JTextField myValueField;
    
    /** The Button that when clicked increases the color value. */
    private final JButton myIncreaseButton;
    
    /** The Button that when clicked decreases the color value. */
    private final JButton myDecreaseButton;
    
    /** The Slider that when adjusted, changes the color value. */
    private final JSlider myValueSlider;
    
    /** The panel that visually displays ONLY the GREEN value for the color. */
    private final JPanel myColorDisplayPanel;
    
    /**
     * Creates a Panel with components used to change and display the Green value for the 
     * backing Color model. 
     * @param theColor the backing model for the system
     */
    public GreenRowPanel(final PropertyChangeEnabledMutableColor theColor) {
        super();
        myColor = theColor;
        myEnableEditButton = new JCheckBox("Enable edit");
        myValueField = new JTextField();
        myIncreaseButton = new JButton();
        myDecreaseButton = new JButton();
        myValueSlider = new JSlider();
        myColorDisplayPanel = new JPanel();
        layoutComponents();
        addListeners();
    }
    
    /**
     * Setup and add the GUI components for this panel. 
     */
    private void layoutComponents() {
        myColorDisplayPanel.setPreferredSize(BUTTON_SIZE);
        myColorDisplayPanel.setBackground(new Color(0, myColor.getGreen(), 0));
        final JLabel rowLabel = new JLabel("Green: ");
        rowLabel.setPreferredSize(LABEL_SIZE);
        myValueField.setText(String.valueOf(myColor.getGreen()));
        myValueField.setEditable(false);
        myValueField.setColumns(TEXT_FIELD_COLUMNS);
        myValueField.setHorizontalAlignment(JTextField.RIGHT);
        
        final JPanel rightPanel = new JPanel();
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 
                                                             HORIZONTAL_PADDING, 
                                                             0, 
                                                             HORIZONTAL_PADDING));
        rightPanel.setBackground(rightPanel.getBackground().darker());
        myIncreaseButton.setIcon(new ImageIcon("./images/ic_increase_value.png"));
        myIncreaseButton.setPreferredSize(BUTTON_SIZE);
        myValueSlider.setMaximum(ColorModel.MAX_VALUE);
        myValueSlider.setMinimum(ColorModel.MIN_VALUE);
        myValueSlider.setValue(myColor.getGreen());
        myValueSlider.setBackground(rightPanel.getBackground());
        myDecreaseButton.setIcon(new ImageIcon("./images/ic_decrease_value.png"));
        myDecreaseButton.setPreferredSize(BUTTON_SIZE);
        myDecreaseButton.setEnabled(false);
        rightPanel.add(myDecreaseButton);
        rightPanel.add(myValueSlider);
        rightPanel.add(myIncreaseButton);
        
        add(myColorDisplayPanel);
        add(rowLabel);
        add(myValueField);
        add(myEnableEditButton);
        add(rightPanel);

    }
    
    /**
     * Add listeners (event handlers) to any GUI components that require handling.  
     */
    private void addListeners() {
        //DO not remove the following statement.
        myColor.addPropertyChangeListener(this);
        myValueField.addActionListener(new ValueFieldActionListener());
        myEnableEditButton.addActionListener(new EnableEditButtonActionListener());
        myIncreaseButton.addActionListener(new IncreaseBottonActionListener());
        myDecreaseButton.addActionListener(new DecreaseBottonActionListener());
        myValueSlider.addChangeListener(new ValueSliderChangeListener());
        

    }
    /**Helper method converts integer to string.
     * @param theInt input integer.
     * @return number as a string.
     */
    private String parseIntToString(final int theInt) {
        return Integer.toString(theInt);  
    }
    /**Helper method updates the value to the field.
     * gets current number from value fields.
     * Enable Increase button when the number smaller than maximum value.
     * Enable Decrease button when the number is greater than minimum value.
     */
    private void updateColor() {
        final int colorNum = Integer.parseInt(myValueField.getText().trim());
        if (colorNum < ColorModel.MAX_VALUE) {
            myIncreaseButton.setEnabled(true);
        }
        if (colorNum > ColorModel.MIN_VALUE) {
            myDecreaseButton.setEnabled(true);
        }
        myColor.setGreen(colorNum);




    }

    /**Helper method reverses the color when invaild field entered.
     * gets current color from myColor.
     * parses the integer to string.
     * sets the color back to myColor.
     * sets the number to value field.
     * updates the current color.
     * 
     * **/
    private void backWardColor() {
        final int currentColor = myColor.getBlue();
        final String currentColorString = parseIntToString(currentColor);
        myColor.setGreen(currentColor);
        myValueField.setText(currentColorString);

        updateColor();
    }  

    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (PROPERTY_GREEN.equals(theEvent.getPropertyName())) {
            myValueField.setText(theEvent.getNewValue().toString());
            myValueSlider.setValue((Integer) theEvent.getNewValue());
            myColorDisplayPanel.
            setBackground(new Color(0, 0, (Integer) theEvent.getNewValue()));
        }
    }
    /** 
     * @author Zheng Zhong
     *  ValueFile ActionListener.
     *  gets the integer from field.
     *  catches format exception return to old color.
     *  calls updateColor method to update color.
     **/
    private class ValueFieldActionListener implements ActionListener {
        @Override

        public void actionPerformed(final ActionEvent theEvent) {
            final int tempNum = Integer.parseInt(myValueField.getText().trim());
            try {
                if (tempNum < ColorModel.MIN_VALUE || tempNum > ColorModel.MAX_VALUE) {
                    backWardColor();
                }
            } catch (final NumberFormatException e) {
                backWardColor();
            }

            updateColor();
        }
    }

    /**
     * @author Zheng Zhong
     *  Increasesbutton ActionListener.
     *  calls update method if interact with this component.
     *  calls parse method to covert string to integer. 
     *  disables increase button when the number beyond minimum value.
     *  calls parse method covert from int to string
     *  Increases the color integer by one.
     *  
     *
     */
    private class IncreaseBottonActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            updateColor();
            int tempNum;
            tempNum = myColor.getGreen();
            if (tempNum >= ColorModel.MAX_VALUE) {
                myIncreaseButton.setEnabled(false);
                tempNum = ColorModel.MAX_VALUE;
            } else {
                myColor.adjustGreen(+1);
            }

        }   
    } 
    /**
     * @author Zheng Zhong
     *  Decreasesbutton ActionListener.
     *  calls update method when interact with this method.
     *  calls parse method to covert string to integer. 
     *  disables decrease button when the number beyond minimum value.
     *  calls parse method covert from int to string
     *  reduces the color integer by one.
     *  
     **/
    private class DecreaseBottonActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            updateColor(); 
            int tempNum;
            tempNum = myColor.getGreen();
            //            tempNum = parseStringToInt(myValueField.getText());
            //            tempNum++;
            if (tempNum <= ColorModel.MIN_VALUE) {
                myDecreaseButton.setEnabled(false);
                tempNum = ColorModel.MIN_VALUE;
            } else {
                myColor.adjustGreen(-1);
            }  

        }

    }
    /**
     * @author Zheng Zhong
     *  EnableEditbutton ActionListener.
     *  calls update method when interact.
     *  Enables editable button when check box is selected.
     *  Disable editable button when check box is diselected.
     **/

    private class EnableEditButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            updateColor();
            if (myEnableEditButton.isSelected()) {
                myValueField.setEditable(true);
            } else {
                myValueField.setEditable(false);
            }


        }
    }
    /**
     * @author Zheng Zhong
     * calls update method when interact.
     *  ValueSlider ChangeListener.
     *  gets the slider value.
     *  calls parse method to covert string to integer. 
     *  sets the new number to value field.
     *  
     **/

    private class ValueSliderChangeListener implements ChangeListener {

        @Override
        public void stateChanged(final ChangeEvent theEvent) {
            updateColor();
            int temp = myValueSlider.getValue();
            if (temp >= ColorModel.MAX_VALUE) {
                myIncreaseButton.setEnabled(false);
                temp = ColorModel.MAX_VALUE;
            } else if (temp <= ColorModel.MIN_VALUE) {
                myDecreaseButton.setEnabled(false);
                temp = ColorModel.MIN_VALUE;
            }

            final String tempString = parseIntToString(temp);

            myValueField.setText(tempString);

        }
    }

}

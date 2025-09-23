package translation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


// TODO Task D: Update the GUI for the program to align with UI shown in the README example.
//            Currently, the program only uses the CanadaTranslator and the user has
//            to manually enter the language code they want to use for the translation.
//            See the examples package for some code snippets that may be useful when updating
//            the GUI.
public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
//            JPanel countryPanel = new JPanel();
//            JTextField countryField = new JTextField(10);
//            countryField.setText("can");
//            countryField.setEditable(false); // we only support the "can" country code for now
//            countryPanel.add(new JLabel("Country:"));
//            countryPanel.add(countryField);

            JPanel languagePanel = new JPanel();
//            JTextField languageField = new JTextField(10);
            languagePanel.add(new JLabel("Language:"));

            JComboBox combo = new JComboBox();
            JSONTranslator translator = new JSONTranslator();
            LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();
            for (String item :  translator.getLanguageCodes()) {
                combo.addItem(languageCodeConverter.fromLanguageCode(item));
            }
            languagePanel.add(combo, 1);


            JPanel buttonPanel = new JPanel();
//            JButton submit = new JButton("Submit");
//            buttonPanel.add(submit);
            JLabel resultLabelText = new JLabel("Translation:");
            buttonPanel.add(resultLabelText);
            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");
            buttonPanel.add(resultLabel);

            // LIST
            CountryCodeConverter converter = new CountryCodeConverter();
            JSONTranslator tl_json = new JSONTranslator();
            String[] items = new String[tl_json.getCountryCodes().size()];
            JComboBox<String> countryComboBox = new JComboBox<>();
            int i = 0;
            for(String countryCode : tl_json.getCountryCodes()) {
                items[i++] = converter.fromCountryCode(countryCode);
            }
            JList<String> list = new JList<>(items);
            list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            JScrollPane scrollPane = new JScrollPane(list);

            JPanel countryPanel = new JPanel();
//            countryPanel.setLayout(new GridLayout(0, 2));
            countryPanel.add(new JLabel(""), 0);
//            JTextField languageField = new JTextField(10);
//            countryPanel.add(new JLabel("Language:"));
//            countryPanel.add(languageField);
            countryPanel.add(scrollPane, 1);


            // adding listener for when the user clicks the submit button
            combo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int[] indices = list.getSelectedIndices();
                    String[] country = new String[indices.length];
//                    String country = countryField.getText();
                    String language = combo.getSelectedItem().toString();
                    String countryCode = languageCodeConverter.fromLanguageCode(language);

                    // for now, just using our simple translator, but
                    // we'll need to use the real JSON version later.
                    Translator translator = new CanadaTranslator();

                    String result = translator.translate(countryCode, country[0]);
                    if (result == null) {
                        result = "no translation found!";
                    }
                    resultLabel.setText(result);

                }

            });

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
//            mainPanel.add(countryPanel);
            mainPanel.add(languagePanel);
            mainPanel.add(buttonPanel);
            mainPanel.add(countryPanel);


            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);


        });
    }
}

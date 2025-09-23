package translation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;


// TODO Task D: Update the GUI for the program to align with UI shown in the README example.
//            Currently, the program only uses the CanadaTranslator and the user has
//            to manually enter the language code they want to use for the translation.
//            See the examples package for some code snippets that may be useful when updating
//            the GUI.
public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // Select language (JComboBox)
            JPanel languagePanel = new JPanel();
            languagePanel.add(new JLabel("Language:"));

            JComboBox<String> combo = new JComboBox<>();
            JSONTranslator translator = new JSONTranslator();
            LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();
            for (String item :  translator.getLanguageCodes()) {
                combo.addItem(languageCodeConverter.fromLanguageCode(item));
            }
            languagePanel.add(combo, 1);

            // Result panel
            JPanel resultPanel = new JPanel();
            JLabel resultLabelText = new JLabel("Translation:");
            resultPanel.add(resultLabelText);
            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");
            resultPanel.add(resultLabel);

            // Select country (JList)
            CountryCodeConverter converter = new CountryCodeConverter();
            JSONTranslator tl_json = new JSONTranslator();
            String[] items = new String[tl_json.getCountryCodes().size()];
            int i = 0;
            for(String countryCode : tl_json.getCountryCodes()) {
                items[i++] = converter.fromCountryCode(countryCode);
            }
            JList<String> list = new JList<>(items);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(list);

            JPanel countryPanel = new JPanel();
            countryPanel.add(new JLabel(""), 0);
            countryPanel.add(scrollPane, 1);

            // Event listener
            combo.addItemListener(e -> translateCountry(list, combo, resultLabel));
            list.addListSelectionListener(e -> translateCountry(list, combo, resultLabel));

            // Main panel
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(languagePanel);
            mainPanel.add(resultPanel);
            mainPanel.add(countryPanel);


            // Implement to frame + display
            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);

        });
    }

    private static void translateCountry(JList<String> list, JComboBox<String> combo, JLabel resultLabel) {
        // gets data from JList
        int[] indices = list.getSelectedIndices();
        String[] country = new String[indices.length];
        for (int i = 0; i < indices.length; i++) {
            country[i] = list.getModel().getElementAt(indices[i]);
        }
        CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
        String countryCode = countryCodeConverter.fromCountry(country[0]).toLowerCase();

        // gets data from combo box
        String language = combo.getSelectedItem().toString();
        LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();
        String languageCode = languageCodeConverter.fromLanguage(language);

        // translates items
        JSONTranslator translator = new JSONTranslator();
        String result = translator.translate(countryCode, languageCode);
        if (result == null) {
            result = "no translation found!";
        }

        resultLabel.setText(result);
    }
}
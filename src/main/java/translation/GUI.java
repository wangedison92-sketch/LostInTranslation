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
            JPanel languagePanel = new JPanel();
            languagePanel.add(new JLabel("Language:"));

            JComboBox<String> combo = new JComboBox<>();
            JSONTranslator translator = new JSONTranslator();
            LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();
            for (String item :  translator.getLanguageCodes()) {
                combo.addItem(languageCodeConverter.fromLanguageCode(item));
            }
            languagePanel.add(combo, 1);

            JPanel resultPanel = new JPanel();
            JLabel resultLabelText = new JLabel("Translation:");
            resultPanel.add(resultLabelText);
            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");
            resultPanel.add(resultLabel);

            // LIST
            CountryCodeConverter converter = new CountryCodeConverter();
            JSONTranslator tl_json = new JSONTranslator();
            String[] items = new String[tl_json.getCountryCodes().size()];
//            JComboBox<String> countryComboBox = new JComboBox<>();
            int i = 0;
            for(String countryCode : tl_json.getCountryCodes()) {
                items[i++] = converter.fromCountryCode(countryCode);
            }
            JList<String> list = new JList<>(items);
            list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            JScrollPane scrollPane = new JScrollPane(list);

            JPanel countryPanel = new JPanel();
            countryPanel.add(new JLabel(""), 0);
            countryPanel.add(scrollPane, 1);

            combo.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    System.out.println(e.getStateChange() + " " +  combo.getSelectedIndex());
                    translateCountry(list, combo, resultLabel);
                }
            });

            list.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    System.out.println(e.getValueIsAdjusting() + " " +  list.getSelectedIndex());
                    translateCountry(list, combo, resultLabel);
                }
            });

//            list.addListSelectionListener(e -> {
//                if (!e.getValueIsAdjusting()) {
//                    translateCountry(list, combo, resultLabel);
//                }
//            });
//            combo.addActionListener(e -> translateCountry(list, combo, resultLabel));



            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(languagePanel);
            mainPanel.add(resultPanel);
            mainPanel.add(countryPanel);


            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);


        });
    }

    private static void translateCountry(JList<String> list, JComboBox<String> combo, JLabel resultLabel) {
        int[] indices = list.getSelectedIndices();
        String[] country = new String[indices.length];
        for (int i = 0; i < indices.length; i++) {
            country[i] = list.getModel().getElementAt(indices[i]);
        }
        CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
        String countryCode = countryCodeConverter.fromCountry(country[0]).toLowerCase();

        String language = combo.getSelectedItem().toString();
        LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();
        String languageCode = languageCodeConverter.fromLanguage(language);

        JSONTranslator translator = new JSONTranslator();
        String result = translator.translate(countryCode, languageCode);
        if (result == null) {
            result = "no translation found!";
        }

//        System.out.println(result + " , lang: " + languageCode + " , country: " + countryCode);

        resultLabel.setText(result);
    }
}
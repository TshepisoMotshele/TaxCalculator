import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.InputMismatchException;

public class TaxCalculatorSwing extends JFrame {

    private JFormattedTextField incomeField;
    private JComboBox<String> ageGroupComboBox;
    private JLabel resultLabel;
    private JLabel monthlyEstimateHeadingLabel;
    private JLabel monthlyEstimateLabel;

    public TaxCalculatorSwing() {
        setTitle("Income Tax Calculator - South Africa (2024)");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel headerLabel = new JLabel("2024 Income Tax Calculator");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(new Color(51, 102, 153)); // Dark Blue text
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(headerLabel, gbc);

        NumberFormat format = DecimalFormat.getInstance();
        format.setGroupingUsed(true);

        incomeField = new JFormattedTextField(format);
        incomeField.setPreferredSize(new Dimension(300, 50));
        incomeField.setBorder(BorderFactory.createTitledBorder("Enter your annual gross salary"));
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(incomeField, gbc);

        ageGroupComboBox = new JComboBox<>();
        ageGroupComboBox.addItem("Under 65");
        ageGroupComboBox.addItem("Between 65 and 75");
        ageGroupComboBox.addItem("75 and older");
        ageGroupComboBox.setBorder(BorderFactory.createTitledBorder("Choose your age group"));
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(ageGroupComboBox, gbc);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.setBackground(new Color(13, 87, 248));
        calculateButton.setForeground(Color.WHITE);
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateAndDisplay();
            }
        });
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(calculateButton, gbc);

        JButton resetButton = new JButton("Reset");
        resetButton.setBackground(new Color(34, 255, 255));
        resetButton.setForeground(Color.WHITE);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFields();
            }
        });
        gbc.gridx = 1;
        panel.add(resetButton, gbc);

        resultLabel = new JLabel();
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        resultLabel.setForeground(new Color(0, 102, 204)); // Dark Blue text
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(resultLabel, gbc);

        monthlyEstimateHeadingLabel = new JLabel("Monthly Estimate");
        monthlyEstimateHeadingLabel.setFont(new Font("Arial", Font.BOLD, 14));
        monthlyEstimateHeadingLabel.setForeground(new Color(51, 102, 153)); // Dark Blue text
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(monthlyEstimateHeadingLabel, gbc);

        monthlyEstimateLabel = new JLabel();
        monthlyEstimateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        monthlyEstimateLabel.setForeground(new Color(0, 102, 204)); // Dark Blue text
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(monthlyEstimateLabel, gbc);

        JLabel disclaimerLabel = new JLabel("<html><i>Disclaimer: This is a demo version; tax rules were not exhaustively applied as per Income Tax Act</i></html>");
        disclaimerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        disclaimerLabel.setForeground(new Color(128, 128, 128)); // Gray text
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        panel.add(disclaimerLabel, gbc);

        add(panel);
        setVisible(true);
    }

    private void calculateAndDisplay() {
        try {
            Number parsedNumber = (Number) incomeField.getFormatter().stringToValue(incomeField.getText());
            double annualIncome = parsedNumber.doubleValue();
            if (annualIncome < 0) {
                throw new NumberFormatException();
            }

            int ageGroup = ageGroupComboBox.getSelectedIndex() + 1;

            double incomeTax = calculateIncomeTax(annualIncome, ageGroup);
            double takeHomePay = annualIncome - incomeTax;

            if (incomeTax == 0) {
                resultLabel.setText("Income Tax: N/A\nTake Home Pay: R" + formatMoney(takeHomePay));
            } else {
                resultLabel.setText(
                        "<html>Income Tax: R" + formatMoney(incomeTax) + "<br>" +
                                "Take Home Pay: R" + formatMoney(takeHomePay) + "<br>" +
                                "Tax Bracket: " + getTaxBracket(incomeTax) + "<br>" +
                                "Age Rebate: R" + formatMoney(calculateRebate(ageGroup)) + "</html>"
                );
            }

            // Calculate PAYE and monthly NET salary
            double paye = incomeTax / 12;
            double monthlyNetSalary = takeHomePay / 12;

            monthlyEstimateLabel.setText(
                    "<html>Monthly PAYE: R" + formatMoney(paye) + "<br>" +
                            "Monthly NET Salary: R" + formatMoney(monthlyNetSalary) + "</html>"
            );

        } catch (ParseException | NumberFormatException | InputMismatchException ex) {
            resultLabel.setText("Invalid input. Please enter a valid positive number for annual income.");
            monthlyEstimateLabel.setText(""); // Clear monthly estimate label
        }
    }

    private void resetFields() {
        incomeField.setText("");
        ageGroupComboBox.setSelectedIndex(0);
        resultLabel.setText("");
        monthlyEstimateLabel.setText("");
    }

    private double calculateIncomeTax(double annualIncome, int ageGroup) {
        double incomeTax = 0;
        double rebate = calculateRebate(ageGroup);

        if (ageGroup == 1 && annualIncome < 95750) {
            System.out.println("Income is not taxable.");
        } else if (ageGroup == 2 && annualIncome < 148217) {
            System.out.println("Income is not taxable.");
        } else if (ageGroup == 3 && annualIncome < 165689) {
            System.out.println("Income is not taxable.");
        } else {
            if (annualIncome <= 237100) {
                incomeTax = 0.18 * annualIncome;
            } else if (annualIncome <= 370500) {
                incomeTax = 42678 + 0.26 * (annualIncome - 237100);
            } else if (annualIncome <= 512800) {
                incomeTax = 77362 + 0.31 * (annualIncome - 370500);
            } else if (annualIncome <= 673000) {
                incomeTax = 121475 + 0.36 * (annualIncome - 512800);
            } else if (annualIncome <= 857900) {
                incomeTax = 179147 + 0.39 * (annualIncome - 673000);
            } else if (annualIncome <= 1817000) {
                incomeTax = 251258 + 0.41 * (annualIncome - 857900);
            } else {
                incomeTax = 644489 + 0.45 * (annualIncome - 1817000);
            }

            incomeTax -= rebate;
        }

        return incomeTax;
    }

    private double calculateRebate(int ageGroup) {
        switch (ageGroup) {
            case 1:
                return 17235;
            case 2:
                return 9444;
            case 3:
                return 3145;
            default:
                return 0;
        }
    }

    private String getTaxBracket(double incomeTax) {
        if (incomeTax <= 42678) {
            return "18%";
        } else if (incomeTax <= 77362) {
            return "26%";
        } else if (incomeTax <= 121475) {
            return "31%";
        } else if (incomeTax <= 179147) {
            return "36%";
        } else if (incomeTax <= 251258) {
            return "39%";
        } else if (incomeTax <= 644489) {
            return "41%";
        } else {
            return "45%";
        }
    }

    private String formatMoney(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###.00");
        return decimalFormat.format(amount);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TaxCalculatorSwing());
    }
}

public class PriceDocument extends javax.swing.text.PlainDocument {
    @Override
    public void insertString(int offs, String str, javax.swing.text.AttributeSet a)
            throws javax.swing.text.BadLocationException {
        if (str == null) return;
        
        String currentText = getText(0, getLength());
        String newText = new StringBuilder(currentText).insert(offs, str).toString();
        
        // Only allow numbers and one decimal point
        if (newText.matches("^\\d*\\.?\\d{0,2}$") && newText.length() <= 10) {
            super.insertString(offs, str, a);
        }
    }
} 
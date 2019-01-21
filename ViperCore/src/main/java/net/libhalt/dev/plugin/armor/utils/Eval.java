package net.libhalt.dev.plugin.armor.utils;

import java.io.*;

public class Eval
{
    public static double eval(final String str) throws NumberFormatException {
        class Parser
        {
            int pos;
            int c;
            
            Parser() {
                this.pos = -1;
            }
            
            void eatChar() {
                this.c = ((++this.pos < str.length()) ? str.charAt(this.pos) : -1);
            }
            
            void eatSpace() {
                while (Character.isWhitespace(this.c)) {
                    this.eatChar();
                }
            }
            
            double parse() {
                this.eatChar();
                final double v = this.parseExpression();
                if (this.c != -1) {
                    throw new NumberFormatException("Unexpected: " + (char)this.c);
                }
                return v;
            }
            
            double parseExpression() {
                double v = this.parseTerm();
                while (true) {
                    this.eatSpace();
                    if (this.c == 43) {
                        this.eatChar();
                        v += this.parseTerm();
                    }
                    else {
                        if (this.c != 45) {
                            break;
                        }
                        this.eatChar();
                        v -= this.parseTerm();
                    }
                }
                return v;
            }
            
            double parseTerm() {
                double v = this.parseFactor();
                while (true) {
                    this.eatSpace();
                    if (this.c == 47) {
                        this.eatChar();
                        v /= this.parseFactor();
                    }
                    else {
                        if (this.c != 42 && this.c != 40) {
                            break;
                        }
                        if (this.c == 42) {
                            this.eatChar();
                        }
                        v *= this.parseFactor();
                    }
                }
                return v;
            }
            
            double parseFactor() {
                boolean negate = false;
                this.eatSpace();
                double v;
                if (this.c == 40) {
                    this.eatChar();
                    v = this.parseExpression();
                    if (this.c == 41) {
                        this.eatChar();
                    }
                }
                else {
                    if (this.c == 43 || this.c == 45) {
                        negate = (this.c == 45);
                        this.eatChar();
                        this.eatSpace();
                    }
                    final StringBuilder sb = new StringBuilder();
                    while ((this.c >= 48 && this.c <= 57) || this.c == 46) {
                        sb.append((char)this.c);
                        this.eatChar();
                    }
                    if (sb.length() == 0) {
                        throw new NumberFormatException("Unexpected: " + (char)this.c);
                    }
                    v = Double.parseDouble(sb.toString());
                }
                this.eatSpace();
                if (this.c == 94) {
                    this.eatChar();
                    v = Math.pow(v, this.parseFactor());
                }
                if (negate) {
                    v = -v;
                }
                return v;
            }
        }
        return new Parser().parse();
    }
}

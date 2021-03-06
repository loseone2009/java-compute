package org.bm.utils;

import org.bm.analysis.exception.MathematicalAnalysisException;

/**
 * Copyright 2012 B. MORIN
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * 
 * Describe an operator, with its String representation, its precedence, and if
 * it is left-associative or not.
 * 
 * <table border="1">
 * <tbody>
 * <tr>
 * <td>15</td>
 * <td>()</td>
 * <td>Grouping</td>
 * </tr>
 * <tr>
 * <td>14</td>
 * <td>&nbsp;! &nbsp; ~ &nbsp; - &nbsp; +&nbsp; ^</td>
 * <td>(most) unary operations</td>
 * </tr>
 * <tr>
 * <td>13</td>
 * <td>&nbsp; *&nbsp; / &nbsp;&nbsp;%</td>
 * <td>Multiplication, division, <a title="Modular arithmetic"
 * href="/wiki/Modular_arithmetic">modulo</a></td>
 * </tr>
 * <tr>
 * <td>12</td>
 * <td>+ &nbsp; -</td>
 * <td>Addition and subtraction</td>
 * </tr>
 * <tr>
 * <td>11</td>
 * <td>&lt;&lt; &nbsp; &gt;&gt;</td>
 * <td>Bitwise shift left and right</td>
 * </tr>
 * <tr>
 * <td>10</td>
 * <td>&lt; &nbsp; &lt;= &nbsp; &gt; &nbsp; &gt;=</td>
 * <td>Comparisons: less-than, ...</td>
 * </tr>
 * <tr>
 * <td>9</td>
 * <td>== &nbsp;&nbsp;!=</td>
 * <td>Comparisons: equal and not equal</td>
 * </tr>
 * <tr>
 * <td>8</td>
 * <td>&amp;</td>
 * <td>Bitwise AND</td>
 * </tr>
 * <tr>
 * <td>7</td>
 * <td>^</td>
 * <td>Bitwise exclusive OR</td>
 * </tr>
 * <tr>
 * <td>6</td>
 * <td>|</td>
 * <td>Bitwise inclusive (normal) OR</td>
 * </tr>
 * <tr>
 * <td>5</td>
 * <td>&amp;&amp;</td>
 * <td>Logical AND</td>
 * </tr>
 * <tr>
 * <td>4</td>
 * <td>||</td>
 * <td>Logical OR</td>
 * </tr>
 * <tr>
 * <td>3</td>
 * <td>&nbsp;?:</td>
 * <td><a title="?:" href="/wiki/%3F:">Conditional expression</a> (<a
 * class="mw-redirect" title="Ternary operator"
 * href="/wiki/Ternary_operator">ternary operator</a>)</td>
 * </tr>
 * <tr>
 * <td>2</td>
 * <td>=&nbsp; += &nbsp; -= &nbsp; *= &nbsp; /= &nbsp;&nbsp;%= &nbsp; &amp;=
 * &nbsp; |= &nbsp; ^= &nbsp; &lt;&lt;= &nbsp; &gt;&gt;=</td>
 * <td>Assignment operators</td>
 * </tr>
 * <tr>
 * <td>1</td>
 * <td>,</td>
 * <td><a title="Comma operator" href="/wiki/Comma_operator">Comma operator</a></td>
 * </tr>
 * </tbody>
 * </table>
 * 
 * @author morinb
 * 
 */

public enum Operator {
   /**
    * The addition operator.
    */
   ADDITION("+", 12, true, 2, new DelegateFunction(2) {
      @Override
      public String compute(String... args) throws MathematicalAnalysisException {
         if (args == null) {
            throw new MathematicalAnalysisException("ADDITION: Args must not be null");
         }
         if (args.length != nbArgs) {
            throw new MathematicalAnalysisException("ADDITION: function needs " + nbArgs + " exactly argument(s).");
         }

         // As the stack stores in LIFO mode, the 2nd parameter is popped
         // first.
         String arg2 = args[0];
         String arg1 = args[1];
         Double d1;
         Double d2;
         try {
            d1 = Double.parseDouble(arg1);
            d2 = Double.parseDouble(arg2);
         } catch (NumberFormatException e) {
            throw new MathematicalAnalysisException("ADDITION: the arguments must be Numbers.", e);
         }

         Double d = d1 + d2;

         return d.toString();
      }
   }),
   /**
    * The substraction operator
    */
   SUBSTRACTION("-", 12, true, 2, new DelegateFunction(2) {
      @Override
      public String compute(String... args) throws MathematicalAnalysisException {
         if (args == null) {
            throw new MathematicalAnalysisException("SUBSTRACTION: Args must not be null");
         }
         if (args.length != nbArgs) {
            throw new MathematicalAnalysisException("SUBSTRACTION: function needs " + nbArgs + " exactly argument(s).");
         }

         // As the stack stores in LIFO mode, the 2nd parameter is popped
         // first.
         String arg2 = args[0];
         String arg1 = args[1];
         Double d1;
         Double d2;
         try {
            d1 = Double.parseDouble(arg1);
            d2 = Double.parseDouble(arg2);
         } catch (NumberFormatException e) {
            throw new MathematicalAnalysisException("SUBSTRACTION: the arguments must be Numbers.", e);
         }

         Double d = d1 - d2;

         return d.toString();
      }
   }),
   /**
    * The substraction operator
    */
   OPPOSITE("_", 14, true, 1, new DelegateFunction(1) {
      @Override
      public String compute(String... args) throws MathematicalAnalysisException {
         if (args == null) {
            throw new MathematicalAnalysisException("MINUS: Args must not be null");
         }
         if (args.length != nbArgs) {
            throw new MathematicalAnalysisException("MINUS: function needs " + nbArgs + " exactly argument(s).");
         }

         // As the stack stores in LIFO mode, the 2nd parameter is popped
         // first.
         String arg1 = args[0];
         Double d1;
         try {
            d1 = Double.parseDouble(arg1);
         } catch (NumberFormatException e) {
            throw new MathematicalAnalysisException("MINUS: the arguments must be Numbers.", e);
         }

         Double d;
         d = -d1;

         return d.toString();
      }
   }),
   /**
    * The multiplication operator.
    */
   MULTIPLICATION("*", 13, true, 2, new DelegateFunction(2) {
      @Override
      public String compute(String... args) throws MathematicalAnalysisException {
         if (args == null) {
            throw new MathematicalAnalysisException("MULTIPLICATION: Args must not be null");
         }
         if (args.length != nbArgs) {
            throw new MathematicalAnalysisException("MULTIPLICATION: function needs " + nbArgs + " exactly argument(s).");
         }

         // As the stack stores in LIFO mode, the 2nd parameter is popped
         // first.
         String arg2 = args[0];
         String arg1 = args[1];
         Double d1;
         Double d2;
         try {
            d1 = Double.parseDouble(arg1);
            d2 = Double.parseDouble(arg2);
         } catch (NumberFormatException e) {
            throw new MathematicalAnalysisException("MULTIPLICATION: the arguments must be Numbers.", e);
         }

         Double d = d1 * d2;

         return d.toString();
      }
   }),
   /**
    * The modulo operator.
    */
   MODULO("%", 13, true, 2, new DelegateFunction(2) {
      @Override
      public String compute(String... args) throws MathematicalAnalysisException {
         if (args == null) {
            throw new MathematicalAnalysisException("MODULO: Args must not be null");
         }
         if (args.length != nbArgs) {
            throw new MathematicalAnalysisException("MODULO: function needs " + nbArgs + " exactly argument(s).");
         }

         // As the stack stores in LIFO mode, the 2nd parameter is popped
         // first.
         String arg2 = args[0];
         String arg1 = args[1];
         Double d1;
         Double d2;
         try {
            d1 = Double.parseDouble(arg1);
            d2 = Double.parseDouble(arg2);
         } catch (NumberFormatException e) {
            throw new MathematicalAnalysisException("MODULO: the arguments must be Numbers.", e);
         }

         Double d = d1 % d2;

         return d.toString();
      }
   }),
   /**
    * The division operator.
    */
   DIVISION("/", 13, true, 2, new DelegateFunction(2) {
      @Override
      public String compute(String... args) throws MathematicalAnalysisException {
         if (args == null) {
            throw new MathematicalAnalysisException("DIVISION: Args must not be null");
         }
         if (args.length != nbArgs) {
            throw new MathematicalAnalysisException("DIVISION: function needs " + nbArgs + " exactly argument(s).");
         }

         // As the stack stores in LIFO mode, the 2nd parameter is popped
         // first.
         String arg2 = args[0];
         String arg1 = args[1];
         Double d1;
         Double d2;
         try {
            d1 = Double.parseDouble(arg1);
            d2 = Double.parseDouble(arg2);
         } catch (NumberFormatException e) {
            throw new MathematicalAnalysisException("DIVISION: the arguments must be Numbers.", e);
         }

         Double d = d1 / d2;

         return d.toString();
      }
   }),
   /**
    * The power operator.
    */
   POWER("^", 14, false, 2, new DelegateFunction(2) {
      @Override
      public String compute(String... args) throws MathematicalAnalysisException {
         if (args == null) {
            throw new MathematicalAnalysisException("POWER: Args must not be null");
         }
         if (args.length != nbArgs) {
            throw new MathematicalAnalysisException("POWER: function needs " + nbArgs + " exactly argument(s).");
         }

         // As the stack stores in LIFO mode, the 2nd parameter is popped
         // first.
         String arg2 = args[0];
         String arg1 = args[1];
         Double d1;
         Double d2;
         try {
            d1 = Double.parseDouble(arg1);
            d2 = Double.parseDouble(arg2);
         } catch (NumberFormatException e) {
            throw new MathematicalAnalysisException("POWER: the arguments must be Numbers.", e);
         }

         Double d;
         d = Math.pow(d1, d2);

         return d.toString();
      }
   });

   private Operator(String valeur, int priorite, boolean leftAssociative, int nbArgs, DelegateFunction delegate) {
      this.value = valeur;
      this.precedence = priorite;
      this.leftAssociative = leftAssociative;
      this.nbArgs = nbArgs;
      this.delegate = delegate;
   }

   public String getValue() {
      return value;
   }

   public int getPrecedence() {
      return precedence;
   }

   public boolean isLeftAssociative() {
      return leftAssociative;
   }

   public boolean isRightAssociative() {
      return !leftAssociative;
   }

   public int getNbArgs() {
      return nbArgs;
   }

   /**
    * Gets an operator by passing its value.
    * 
    * @param value
    *            An operator string value, i.e. + - /
    * @return the operator corresponding to the value parameter. Or null if no
    *         operator was found.
    */
   public static Operator get(String value) {
      Operator[] operators = Operator.values();

      for (Operator operator : operators) {
         String op = operator.value;
         if (op.equals(value)) {
            return operator;
         }
      }

      return null;
   }

   private final String value;

   private final int precedence;

   private final boolean leftAssociative;

   private final int nbArgs;

   private final DelegateFunction delegate;

   @Override
   public String toString() {
      return value;
   }

   public String compute(String... args) throws MathematicalAnalysisException {
      return delegate.compute(args);
   }

}

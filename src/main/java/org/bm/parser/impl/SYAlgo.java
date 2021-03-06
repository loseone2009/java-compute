package org.bm.parser.impl;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.bm.FormulaCompute;
import org.bm.analysis.exception.MathematicalAnalysisException;
import org.bm.parser.RPNParser;
import org.bm.utils.ComputeUtils;
import org.bm.utils.Operator;

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
 * @author morinb
 */
public class SYAlgo implements RPNParser {

   private static final Logger logger = Logger.getLogger(SYAlgo.class);

   /**
    * If true, the parser will output some parsing info.
    */
   private final boolean enableLogging;

   /**
    * A map of variables with their value. If the value is null, the variable
    * stays as is.
    */
   private final Map<String, String> variables;

   public SYAlgo(boolean enableLogging, Map<String, String> variables) {
      this.enableLogging = enableLogging;
      this.variables = variables;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.bm.Parser#parse(java.lang.String)
    */
   @Override
   public List<String> parse(String formula) throws MathematicalAnalysisException {
      if (enableLogging) {
         if (logger.isInfoEnabled()) {
            log("Formula : " + formula);
         }
      }
      formula = format(formula);
      log("Formatted formula : " + formula);
      List<String> outputQueue = new LinkedList<>();
      Deque<String> stack = new LinkedList<>();

      StringTokenizer st = new StringTokenizer(formula, " ");
      String[] tokens = new String[st.countTokens()];
      int i = 0;
      while (st.hasMoreTokens()) {
         tokens[i++] = st.nextToken();
      }

      return analyze(tokens, outputQueue, stack);
   }

   /**
    * Analyze the tokens of the formula. Parse the token using que
    * Shunting-Yard Algorithm (Source Wikipedia):
    * 
    * <ul>
    * <li>Read a token.</li>
    * <li>If the token is a number, then add it to the output queue.</li>
    * <li>If the token is a function token, then push it onto the stack.</li>
    * <li>If the token is a function argument separator (e.g., a comma):</li>
    * </ul>
    * <dl>
    * <dd>
    * <ul>
    * <li>Until the token at the top of the stack is a left parenthesis, pop
    * operators off the stack onto the output queue. If no left parentheses are
    * encountered, either the separator was misplaced or parentheses were
    * mismatched.</li>
    * </ul>
    * </dd>
    * </dl>
    * <ul>
    * <li>If the token is an operator, o<sub>1</sub>, then:</li>
    * </ul>
    * <dl>
    * <dd>
    * <ul>
    * <li>while there is an operator token, o<sub>2</sub>, at the top of the
    * stack, and</li>
    * </ul>
    * <dl>
    * <dd>
    * <dl>
    * <dd>
    * <dl>
    * <dd>either o<sub>1</sub> is left-associative and its precedence is less
    * than or equal to that of o<sub>2</sub>,</dd>
    * <dd>or o<sub>1</sub> is right-associative and its precedence is less than
    * that of o<sub>2</sub>,</dd>
    * </dl>
    * </dd>
    * <dd>pop o<sub>2</sub> off the stack, onto the output queue;</dd>
    * </dl>
    * </dd>
    * </dl>
    * <ul>
    * <li>push o<sub>1</sub> onto the stack.</li>
    * </ul>
    * </dd>
    * </dl>
    * <ul>
    * <li>If the token is a left parenthesis, then push it onto the stack.</li>
    * <li>If the token is a right parenthesis:</li>
    * </ul>
    * <dl>
    * <dd>
    * <ul>
    * <li>Until the token at the top of the stack is a left parenthesis, pop
    * operators off the stack onto the output queue.</li>
    * <li>Pop the left parenthesis from the stack, but not onto the output
    * queue.</li>
    * <li>If the token at the top of the stack is a function token, pop it onto
    * the output queue.</li>
    * <li>If the stack runs out without finding a left parenthesis, then there
    * are mismatched parentheses.</li>
    * </ul>
    * </dd>
    * </dl>
    * </dd> </dl>
    * <ul>
    * <li>When there are no more tokens to read:</li>
    * </ul>
    * <dl>
    * <dd>
    * <ul>
    * <li>While there are still operator tokens in the stack:</li>
    * </ul>
    * <dl>
    * <dd>
    * <ul>
    * <li>If the operator token on the top of the stack is a parenthesis, then
    * there are mismatched parentheses.</li>
    * <li>Pop the operator onto the output queue.</li>
    * </ul>
    * </dd>
    * </dl>
    * </dd>
    * </dl>
    * <ul>
    * <li>Exit.</li>
    * </ul>
    * Example :
    * <table border="1">
    * <caption>Input: 3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3</caption>
    * <tr>
    * <th>Token</th>
    * <th>Action</th>
    * <th>Output (in RPN)</th>
    * <th>Operator Stack</th>
    * <th>Notes</th>
    * </tr>
    * 
    * <tr>
    * <td>3</td>
    * <td>Add token to output</td>
    * <td>3</td>
    * <td>&nbsp;</td>
    * <td>&nbsp;</td>
    * </tr>
    * <tr>
    * <td>+</td>
    * <td>Push token to stack</td>
    * <td>3</td>
    * 
    * <td align="right">+</td>
    * <td>&nbsp;</td>
    * </tr>
    * <tr>
    * <td>4</td>
    * <td>Add token to output</td>
    * <td>3 4</td>
    * <td align="right">+</td>
    * <td>&nbsp;</td>
    * </tr>
    * <tr>
    * <td>&nbsp;</td>
    * 
    * <td>Push token to stack</td>
    * <td>3 4</td>
    * <td align="right">+</td>
    * <td>has higher precedence than +</td>
    * </tr>
    * <tr>
    * <td>2</td>
    * <td>Add token to output</td>
    * <td>3 4 2</td>
    * <td align="right">+</td>
    * 
    * <td>&nbsp;</td>
    * </tr>
    * <tr>
    * <td rowspan="2">/</td>
    * <td>Pop stack to output</td>
    * <td>3 4 2 *</td>
    * <td align="right">+</td>
    * <td>/ and * have same precedence</td>
    * </tr>
    * <tr>
    * <td>Push token to stack</td>
    * 
    * <td>3 4 2 *</td>
    * <td align="right">/ +</td>
    * <td>/ has higher precedence than +</td>
    * </tr>
    * <tr>
    * <td>(</td>
    * <td>Push token to stack</td>
    * <td>3 4 2 *</td>
    * <td align="right">( / +</td>
    * <td>&nbsp;</td>
    * 
    * </tr>
    * <tr>
    * <td>1</td>
    * <td>Add token to output</td>
    * <td>3 4 2 * 1</td>
    * <td align="right">( / +</td>
    * <td>&nbsp;</td>
    * </tr>
    * <tr>
    * <td>-</td>
    * <td>Push token to stack</td>
    * 
    * <td>3 4 2 * 1</td>
    * <td align="right">- ( / +</td>
    * <td>&nbsp;</td>
    * </tr>
    * <tr>
    * <td>5</td>
    * <td>Add token to output</td>
    * <td>3 4 2 * 1 5</td>
    * <td align="right">- ( / +</td>
    * <td>&nbsp;</td>
    * </tr>
    * 
    * <tr>
    * <td rowspan="2">)</td>
    * <td>Pop stack to output</td>
    * <td>3 4 2 * 1 5 -</td>
    * <td align="right">( / +</td>
    * <td>Repeated until "(" found</td>
    * </tr>
    * <tr>
    * <td>Pop stack</td>
    * <td>3 4 2 * 1 5 -</td>
    * 
    * <td align="right">/ +</td>
    * <td>Discard matching parenthesis</td>
    * </tr>
    * <tr>
    * <td>^</td>
    * <td>Push token to stack</td>
    * <td>3 4 2 * 1 5 -</td>
    * <td align="right">^ / +</td>
    * <td>^ has higher precedence than /</td>
    * </tr>
    * 
    * <tr>
    * <td>2</td>
    * <td>Add token to output</td>
    * <td>3 4 2 * 1 5 - 2</td>
    * <td align="right">^ / +</td>
    * <td>&nbsp;</td>
    * </tr>
    * <tr>
    * <td>^</td>
    * <td>Push token to stack</td>
    * <td>3 4 2 * 1 5 - 2</td>
    * 
    * <td align="right">^ ^ / +</td>
    * <td>^ is evaluated right-to-left</td>
    * </tr>
    * <tr>
    * <td>3</td>
    * <td>Add token to output</td>
    * <td>3 4 2 * 1 5 - 2 3</td>
    * <td align="right">^ ^ / +</td>
    * <td>&nbsp;</td>
    * </tr>
    * <tr>
    * 
    * <td><i>end</i></td>
    * <td>Pop entire stack to output</td>
    * <td>3 4 2 * 1 5 - 2 3 ^ ^ / +</td>
    * <td>&nbsp;</td>
    * <td>&nbsp;</td>
    * </tr>
    * </table>
    * 
    * 
    * @param tokens
    *            an array of String tokens
    * @param queue
    *            the outputQueue
    * @param stack
    *            the operator stack
    * @return a RPN notation String of the formula.
    */
   private List<String> analyze(String[] tokens, List<String> queue, Deque<String> stack) throws MathematicalAnalysisException {
      String lastToken = null;

      for (String token : tokens) {
         log("Treatment of token '" + token + "'.");
         //log("Last token : "+lastToken);

         if (isNumber(token)) {
            log("Token " + token + " is a number. Adding to Queue.");
            queue.add(token);
            lastToken = token;
            continue;
         }

         else if (isVariable(token)) {
            log("Token " + token + " is a variable. Adding to Queue.");
            if (null != variables.get(token)) {
               String value = variables.get(token);
               String newValue = FormulaCompute.compute(value, variables, false);
               log("\tReplacing variable " + token + " by its value " + value + " = " + newValue);
               queue.add(newValue);
            } else {
               queue.add(token);
            }
            lastToken = token;
            continue;
         }

         else if (ComputeUtils.isOperator(token)) {
            log("Token " + token + " is an operator.");
            if ("(".equals(lastToken)) {
               if ("-".equals(token)) {
                  // not substraction operator, but opposite operator
                  log("Token " + token + " is the opposite operator.");
                  token = "_";
               }
            }

            if (ComputeUtils.isOperator(stack.peek())) {

               Operator o1 = Operator.get(token);
               String peek = stack.peek();
               Operator o2 = Operator.get(peek);

               if ((o1.getPrecedence() <= o2.getPrecedence() && o1.isLeftAssociative())
                  || (o1.getPrecedence() < o2.getPrecedence() && o1.isRightAssociative())) {
                  if (enableLogging) {
                     if (o1.isLeftAssociative()) {
                        log(token + " priority is <= " + peek + " priority and " + token + " is left-associative");
                     }
                     if (o1.isRightAssociative()) {
                        log(token + " priority is <= " + peek + " priority and " + token + " is right-associative");
                     }
                     if (logger.isInfoEnabled()) {
                        log("Poping " + peek + " from the stack, and adding it to the queue.");
                     }
                  }

                  queue.add(stack.pop());
               } else {
                  if (o1.getPrecedence() > o2.getPrecedence()) {
                     log(token + " priority is > " + peek + " priority");
                  }
               }
            }
            log("Pushing " + token + " onto the Stack.");
            stack.push(token);

         }

         else if (ComputeUtils.isFunction(token)) {
            if (enableLogging) {
               if (logger.isInfoEnabled()) {
                  log("Token " + token + " is an function. Pushing onto the Stack.");
               }
            }
            stack.push(token);
            lastToken = token;
            continue;
         }

         else if (isFunctionArgSeparator(token)) {
            if (enableLogging) {
               if (logger.isInfoEnabled()) {
                  log("Token " + token + " is an function arg separator.");
               }
            }
            while (!"(".equals(stack.peek())) {
               String pop = stack.pop();
               if (enableLogging) {
                  if (logger.isInfoEnabled()) {
                     log("Pop " + pop + " from stack, adding it to Queue.");
                  }
               }
               queue.add(pop);
               if (stack.isEmpty()) {
                  // Erreur
                  throw new MathematicalAnalysisException("Erreur A : parenthesis problem.");
               }
            }
            lastToken = token;
            continue;
         }

         else if ("(".equals(token)) {
            if (enableLogging) {
               if (logger.isInfoEnabled()) {
                  log("Pushing " + token + " onto the stack");
               }
            }
            stack.push(token);
            lastToken = token;
            continue;
         }

         else if (")".equals(token)) {
            if (enableLogging) {
               if (logger.isInfoEnabled()) {
                  log("Until ( is found on the stack, pop token from the stack to the queue.");
               }
            }
            while (!"(".equals(stack.peek())) {
               String pop = stack.pop();
               log("\tAdding " + pop + " to the queue.");
               queue.add(pop);
            }
            log("( found. Dismiss from the stack.");
            stack.pop(); // on enleve la (, et on ne la stocke pas.
            if (ComputeUtils.isFunction(stack.peek())) {
               String peek = stack.peek();
               log("Token " + peek + " is a function, pop it from the stack to the queue.");
               queue.add(stack.pop());
            }
         } else {
            log(token + " unknown. Maybe a variable ?. Added to queue.");
            queue.add(token);
         }
         lastToken = token;
      }

      log("No more token to read.");
      while (!stack.isEmpty()) {
         if ("(".equals(stack.peek())) {
            throw new MathematicalAnalysisException("Erreur C : probleme de parentheses.");
         }
         String pop = stack.pop();
         log("Popping " + pop + " from the stack to the queue.");
         queue.add(pop);
      }

      return queue;
   }

   private void log(String message) {
      if (enableLogging) {
         if (logger.isInfoEnabled()) {
            logger.info(message);
         }
      }
   }

   private boolean isVariable(String token) {
      return variables.keySet().contains(token);
   }

   private boolean isFunctionArgSeparator(String token) {
      return (",".equals(token));
   }

   private boolean isNumber(String token) {
      boolean isNumber;
      try {
         Double.parseDouble(token);
         isNumber = true;
      } catch (NumberFormatException nfe) {
         isNumber = false;
      }
      return isNumber;
   }

   /**
    * Add spaces around operators, comma, and parenthesis.
    * 
    * @param calcul
    *            the String to format
    * @return formatted String
    */
   private String format(String calcul) {
      calcul = calcul.replace("(", " ( ");
      calcul = calcul.replace(")", " ) ");
      calcul = calcul.replace(",", " , ");
      Operator[] ops = Operator.values();

      for (Operator op : ops) {
         calcul = calcul.replace(op.getValue(), " " + op.getValue() + " ");
      }

      calcul = calcul.replaceAll("\\s+", " ");

      return calcul.trim();
   }

}

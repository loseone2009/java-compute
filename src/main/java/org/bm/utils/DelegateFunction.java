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
 * @author morinb
 */
public abstract class DelegateFunction {
   protected final int nbArgs;

   public DelegateFunction(int nbArgs) {
      super();
      this.nbArgs = nbArgs;
   }

   /**
    * Make the computation describe by the implementing class.
    * It takes nbArgs arguments defined by the constructor.
    * @param args The arguments passed to the function.
    * @return The result of the computation.
    * @throws MathematicalAnalysisException
    */
   public abstract String compute(String... args) throws MathematicalAnalysisException;

}

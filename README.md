# Compiler

### 项目结构

```
├─start-symbol						// SLR Parsing Table Maker 的in之一，存放起始符
├─terminals							// SLR Parsing Table Maker 的in之一，存放终结符，空白符分隔
├─non-terminals						// SLR Parsing Table Maker 的in之一，存放非终结符，空白符分隔
├─rules								// SLR Parsing Table Maker 的in之一，存放产生式，每行一条，空白符分隔，第一个字符串为产生式左边
├─test1.tok							// ex1 lex语法分析器的out
├─test1.toy							// ex1 lex语法分析器的in
├─out	// 输出文件(.class)
└─src	// 源代码(.java)
    └─com
        └─jeff
            ├─lex					// ex1词法分析器
            ├─interpreter			// ex4解释器
            └─parser			// 语法分析器
                ├─ll				// ex2 LL(1) 语法分析器
                └─lr			// SLR相关
                    ├─parser		// ex3 SLR 语法分析器
                    └─table			// SLR 解析表生成工具
```

### 项目结构详解

```
└─src													// 源代码
    └─com
        └─jeff
            │  FileUtils.java									// 文件工具类，负责文件IO
            │  StringUtils.java									// 字符串工具类，负责字符串分隔
            │
            ├─interpreter									// 解释器(ex4)
            │      ExpressionException.java						// 表达式计算异常
            │      InterpreterEx2Grammar.java					// 采用ex2语法的解释器
            │      InterpreterEx4Grammar.java					// 采用ex4语法的解释器
            │      InterpreterException.java					// 解释异常
            │      Lex.java										// 解释器用到的词法分析器
            │      Main.java									// Main类实现对Interpreter的调用
            │      NotMatchedException.java						// Token不匹配异常
            │      Token.java									// Token对象，除了实现了Token的match功能以外其他和POJO无异
            │      TokenType.java								// TokenType，枚举类型，存放了可能出现的所有Token类型
            │      TypeCheckException.java						// 类型检查异常，不允许将real赋值给int类型。
            │      VariableNotExistException.java				// 变量不存在异常
            │      VariableTable.java							// 变量符号表
            │
            ├─lex
            │      LexicalAnalyzer.java						// 词法分析器(ex1)
            │      Main.java									// 词法分析器主体，实现了对token判别类型
            │      Token.java									// Token的POJO对象类
            │      Tokenizer.java								// Token分词器，分词规则为((number identifier?)|identifier)
            │
            └─parser
                │  NonTerminal.java							// 非终结符的POJO对象类
                │  ProductionRule.java						// 产生式的POJO对象类
                │  Symbol.java								// 符号的POJO对象类
                │  Terminal.java							// 终结符的POJO对象类
                │
                ├─ll									// LL(1)解释器(ex2)
                │      LLParser.java						// LL解释器主体，实现从文本到AST的解析
                │      LLParsingTable.java					// LL解析表
                │      Main.java							// Main类实现对LLParser的调用
                │
                └─lr									// SLR相关
                    │  Main.java							// Main类实现对SLR的调用
                    │
                    ├─parser								// SLR解释器
                    │      LRConstant.java						// SLR的起始符号，终结符，非终结符，解析表
                    │      LRParser.java						// SLR解析器
                    │      SLRParsingException.java				// SLR解析异常
                    │
                    └─table									// SLR 解析表生成工具
                            Action.java							// ACTION表中的活动信息POJO对象类，如Accept,Shift n,Reduce n
                            ActionTable.java					// SLR解析表中的ACTION表部分
                            ActionType.java						// 活动表中的活动类型枚举，含ACCEPT,SHIFT,REDUCE
                            CanonicalCollection.java			// 项目集规范族对象类
                            Closure.java						// 项目集闭包对象类
                            GotoTable.java						// SLR解析表中的GOTO表部分
                            Item.java							// SLR Item对象类,如 program -> .compoundstmt
                            Processor.java						// 预处理器，计算所有非终结符的FirstSet和FollowSet
                            SLRActionTableConflictException.java// SLR活动表冲突异常(移入-规约冲突 或 规约-规约冲突)
                            SLRParsingTable.java				// SLR 解析表对象类
                            SLRParsingTableBuilder.java			// SLR 解析表创建工具类
```


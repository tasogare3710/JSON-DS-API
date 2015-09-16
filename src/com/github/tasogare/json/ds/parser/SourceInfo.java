// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.parser;

/**
 * XXX: 今のところこのクラスを使っているのはParserExceptionだけ
 * <p>
 * このクラスはソースコードの情報を持ちます。
 * <p>
 * {@link ParserException}が発生したときにこのオブジェクトを利用すればソースコードの問題のある箇所を表示することが出来ます。
 * 
 * <pre>
 * final String name = "ソースコード名";
 * final InputStream is = ...;
 * try (BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
 *     final TokenStream ts = new TokenStream(new Source(r));
 *     final Parser parser = new Parser(ts, name);
 *     parser.parse();
 * } catch (ParserException e) {
 *     final SourceInfo info = e.getSourceInfo();
 *     System.err.println(info.getSourceName());
 *     System.err.println("row: " + info.getRow() + " col: " + info.getColumn());
 *     // 範囲 [ lineStart, position ) でJSON-DS Errorの発生したソースコードの一行を求めることが出来ます
 *     System.err.println(info.getSource().renge(info.getLineStart(), info.getPosition()));
 *     final StringBuilder sb = new StringBuilder();
 *     for (int i = 0; i &lt; info.getColumn() - 1; i++) {
 *         sb.append(" ");
 *     }
 *     System.err.println(sb.append("^").toString());
 *     e.printStackTrace();
 * }
 * </pre>
 * 
 * @author tasogare
 *
 */
public class SourceInfo implements java.io.Serializable {

    private static final long serialVersionUID = 973690707737892499L;

    private final int row;
    private final int column;
    private final int lineStart;
    private final int position;
    private final String sourceName;
    private final Source source;

    public SourceInfo(int row, int column, int lineStart, int position, String sourceName, Source source) {
        this.row = row;
        this.column = column;
        this.lineStart = lineStart;
        this.position = position;
        this.sourceName = sourceName;
        this.source = source;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(row, column, lineStart, position, sourceName, source);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SourceInfo)) {
            return false;
        }
        SourceInfo other = (SourceInfo) obj;
        if (column != other.column) {
            return false;
        }
        if (lineStart != other.lineStart) {
            return false;
        }
        if (position != other.position) {
            return false;
        }
        if (row != other.row) {
            return false;
        }
        if (source == null) {
            if (other.source != null) {
                return false;
            }
        } else if (!source.equals(other.source)) {
            return false;
        }
        if (sourceName == null) {
            if (other.sourceName != null) {
                return false;
            }
        } else if (!sourceName.equals(other.sourceName)) {
            return false;
        }
        return true;
    }

    /**
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return the column
     */
    public int getColumn() {
        return column;
    }

    /**
     * @return the lineStart
     */
    public int getLineStart() {
        return lineStart;
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @return the sourceName
     */
    public String getSourceName() {
        return sourceName;
    }

    /**
     * @return the source
     */
    public Source getSource() {
        return source;
    }
}

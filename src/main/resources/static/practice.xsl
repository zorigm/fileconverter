<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns="http://www.w3.org/1999/xhtml">

    <xsl:template match="IdMappings" mode="css-rule">
        <xsl:text>/* Styles */&#10;</xsl:text>
        <xsl:apply-templates select="Style" mode="css-rule" />
        <xsl:text>/* Paragraph attributes */&#10;</xsl:text>
        <xsl:apply-templates select="ParaShape" mode="css-rule" />
        <xsl:text>/* Text attributes */&#10;</xsl:text>
        <xsl:apply-templates select="CharShape" mode="css-rule" />
        <xsl:apply-templates select="BorderFill" mode="css-rule" />
        <xsl:apply-templates select="Bullet" mode="css-rule" />
    </xsl:template>

    <xsl:template match="Style" mode="css-rule">
        <xsl:variable name="paragraph-selector">
            <xsl:text>.</xsl:text>
            <xsl:value-of select="translate(@name, ' ', '-')"/>
        </xsl:variable>
        <xsl:variable name="spans-selector">
            <xsl:value-of select="$paragraph-selector" />
            <xsl:text> &gt; </xsl:text>
            <xsl:text>span</xsl:text>
        </xsl:variable>
        <xsl:if test="@kind = 'paragraph'">
            <xsl:variable name="parashape_pos" select="@parashape-id + 1" />
            <xsl:variable name="parashape" select="//ParaShape[$parashape_pos]" />
            <xsl:call-template name="css-rule">
                <xsl:with-param name="selector" select="$paragraph-selector" />
                <xsl:with-param name="declarations">
                    <xsl:text>/* </xsl:text>
                    <xsl:text>@parashape-id = </xsl:text>
                    <xsl:value-of select="@parashape-id" />
                    <xsl:text>*/&#10;</xsl:text>
                    <xsl:apply-templates select="$parashape" mode="css-declaration" />
                </xsl:with-param>
            </xsl:call-template>
            <xsl:call-template name="css-rule">
                <xsl:with-param name="selector" select="$spans-selector" />
                <xsl:with-param name="declarations">
                    <xsl:apply-templates select="$parashape" mode="css-declaration-for-span" />
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:text>/* </xsl:text>
        <xsl:text>@charshape-id = </xsl:text>
        <xsl:value-of select="@charshape-id" />
        <xsl:text>*/&#10;</xsl:text>
        <xsl:variable name="charshape_pos" select="number(@charshape-id) + 1" />
        <xsl:variable name="charshape" select="//CharShape[$charshape_pos]" />
        <xsl:for-each select="$charshape">
            <xsl:call-template name="charshape-css-rule">
                <xsl:with-param name="charshape-selector" select="$spans-selector" />
            </xsl:call-template>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="ParaShape" mode="css-rule">
        <xsl:variable name="paragraph-selector">
            <xsl:text>p.parashape-</xsl:text>
            <xsl:number value="position()-1" />
        </xsl:variable>
        <xsl:variable name="spans-selector">
            <xsl:value-of select="$paragraph-selector" />
            <xsl:text> &gt; </xsl:text>
            <xsl:text>span</xsl:text>
        </xsl:variable>
        <xsl:call-template name="css-rule">
            <xsl:with-param name="selector" select="$paragraph-selector" />
            <xsl:with-param name="declarations">
                <xsl:apply-templates select="." mode="css-declaration" />
            </xsl:with-param>
        </xsl:call-template>

        <xsl:call-template name="css-rule">
            <xsl:with-param name="selector" select="$spans-selector" />
            <xsl:with-param name="declarations">
                <xsl:apply-templates select="." mode="css-declaration-for-span" />
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="ParaShape" mode="css-declaration">
        <xsl:call-template name="css-declaration">
            <xsl:with-param name="property">margin</xsl:with-param>
            <xsl:with-param name="value">
                <xsl:call-template name="hwpunit-to-pt">
                    <xsl:with-param name="hwpunit" select="@doubled-margin-top div 2" />
                </xsl:call-template>
                <xsl:text> </xsl:text>
                <xsl:call-template name="hwpunit-to-pt">
                    <xsl:with-param name="hwpunit" select="@doubled-margin-right div 2" />
                </xsl:call-template>
                <xsl:text> </xsl:text>
                <xsl:call-template name="hwpunit-to-pt">
                    <xsl:with-param name="hwpunit" select="@doubled-margin-bottom div 2" />
                </xsl:call-template>
                <xsl:text> </xsl:text>
                <xsl:call-template name="hwpunit-to-pt">
                    <xsl:with-param name="hwpunit" select="@doubled-margin-left div 2" />
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>
        <xsl:apply-templates select="." mode="text-align" />
        <xsl:apply-templates select="." mode="text-indent" />
        <xsl:choose>
            <xsl:when test="@linespacing-type = 'ratio'">
                <xsl:call-template name="css-declaration">
                    <xsl:with-param name="property">min-height</xsl:with-param>
                    <xsl:with-param name="value">
                        <xsl:value-of select="@linespacing div 100" />
                        <xsl:text>em</xsl:text>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:when>
        </xsl:choose>
    </xsl:template>


  <xsl:template match="/">
    <xsl:element name="html">
        <xsl:element name="head">
        </xsl:element>
        <xsl:element name="body">
            <xsl:text>TEXT</xsl:text>
        </xsl:element>
    </xsl:element>
  </xsl:template>

  <xsl:template match="hs\sec" mode="html">
    <xsl:element name="html">
      <xsl:element name="head">
        <xsl:element name="meta">
          <xsl:attribute name="http-equiv">content-type</xsl:attribute>
          <xsl:attribute name="content">text/html; charset=utf-8</xsl:attribute>
        </xsl:element>
        <xsl:apply-templates select="HwpSummaryInfo" mode="head" />
        <xsl:apply-templates select="DocInfo" mode="head" />
        <xsl:element name="style">
          <xsl:attribute name="type">text/css</xsl:attribute>
          <xsl:text>&#10;</xsl:text>
          <xsl:apply-templates select="BodyText/SectionDef" mode="css-rule" />
          <xsl:apply-templates select="//Header" mode="css-rule" />
          <xsl:apply-templates select="//Footer" mode="css-rule" />
        </xsl:element>
      </xsl:element>
      <xsl:apply-templates select="BodyText" mode="body" />
    </xsl:element>
  </xsl:template>

    <xsl:template match="SectionDef" mode="div">
    <xsl:element name="div">
      <xsl:attribute name="class">
        <xsl:text>Section</xsl:text>
        <xsl:text> </xsl:text>
        <xsl:text>Section-</xsl:text>
        <xsl:value-of select="@section-id" />
        <xsl:text> </xsl:text>
        <xsl:text>Paper</xsl:text>
      </xsl:attribute>
      <xsl:element name="div">
        <xsl:attribute name="class">HeaderPageFooter</xsl:attribute>
        <xsl:apply-templates select="//Header" mode="div" />
        <xsl:element name="div">
          <xsl:attribute name="class">Page</xsl:attribute>
          <xsl:apply-templates />
        </xsl:element>
        <xsl:apply-templates select="//Footer" mode="div" />
      </xsl:element>
    </xsl:element>
  </xsl:template>
</xsl:stylesheet>  
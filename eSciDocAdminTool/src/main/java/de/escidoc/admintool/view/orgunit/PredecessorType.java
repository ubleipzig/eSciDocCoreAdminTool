/**
 * 
 */
package de.escidoc.admintool.view.orgunit;

/**
 * @author ASP
 * 
 */
public enum PredecessorType {
    BLANK("", "de.escidoc.admintool.view.orgunit.editor.BlankPredecessorEditor"), SPLITTING(
        "Splitting",
        "de.escidoc.admintool.view.orgunit.editor.SplittingPredecessorEditor"), FUSION(
        "Fusion",
        "de.escidoc.admintool.view.orgunit.editor.FusionPredecessorEditor"), SPIN_OFF(
        "Spin-off",
        "de.escidoc.admintool.view.orgunit.editor.SpinOffPPredecessorEditor"), AFFILIATION(
        "Affiliation",
        "de.escidoc.admintool.view.orgunit.editor.AffiliationPredecessorEditor"), REPLACEMENT(
        "Replacement",
        "de.escidoc.admintool.view.orgunit.editor.ReplacementPredecessorEditor");

    private String name;

    private String exec;

    private PredecessorType(String name, String exec) {
        this.name = name;
        this.exec = exec;
    }

    @Override
    /** The name for displaying purpose.
     * @return The name for displaying purpose.
     */
    public String toString() {
        return name;
    }

    /**
     * Returns the full qualified class path for reflection.
     * 
     * @return the class path.
     */
    public String getExecutionClass() {
        return exec;
    }

}

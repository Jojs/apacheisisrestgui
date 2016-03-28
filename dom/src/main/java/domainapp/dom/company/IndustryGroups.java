package domainapp.dom.company;

public enum IndustryGroups {

    BASIC_INDUSTRIES("fa fa-gear fa-lg"),
    CAPITAL_GOODS("fa fa-wrench fa-lg"),
    CONSUMER_DURABLES("fa fa-shopping-cart fa-lg"),
    CONSUMER_NON_DURABLES("fa fa-tatags.pnggs fa-lg"),
    CONSUMER_SERVICES("fa fa-users fa-lg"),
    ENERGY("fa fa-plug fa-lg"),
    FINANCE("fa fa-money fa-lg"),
    HEALTHCARE("fa fa-hospital-o fa-lg"),
    MISCELLANEOUS("fa fa-briefcase fa-lg"),
    PUBLIC_UTILITIES("fa fa-lightbulb-o fa-lg"),
    TECHNOLOGY("fa fa-laptop fa-lg"),
    TRANSPORTATION("fa fa-car fa-lg");

    private String fontAwesome;

    IndustryGroups(final String fontAwesome) {

        this.fontAwesome = fontAwesome;
    }

    public String icon() {
        return fontAwesome;
    }
}

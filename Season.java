/**
 * Season class.
 */

public class Season {
    private final int startYear;
    private final int endYear;

    /**
     * constructor from one year.
     * @param start the starting year.
     */
    Season(int start) {
        this.startYear = start;
        this.endYear = start;
    }

    /**
     * constructor from 2 years. used for bi-annual seasons.
     * @param start the year the season starts.
     * @param end the year the season ends.
     */
    Season(int start, int end) {
        this.startYear = start;
        this.endYear = end;
    }

    @Override
    public String toString() {
        if (this.startYear == this.endYear) {
            return String.valueOf(this.startYear);
        }
        return this.startYear + "-" + this.endYear;
    }

    /**
     * get the next season.
     * @return Season.
     */
    public Season nextSeason() {
        if (this.startYear == this.endYear) {
            return new Season(this.startYear + 1);
        }
        return new Season(this.endYear, this.endYear + 1);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        }
        Season o = (Season) other;
        return (this.startYear == o.getStartYear() && this.endYear == o.getEndYear());
    }

    /**
     * get the year the season started in.
     * @return year.
     */
    public int getStartYear() {
        return this.startYear;
    }

    /**
     * get the year the season ended in.
     * @return year.
     */
    public int getEndYear() {
        return this.endYear;
    }
}

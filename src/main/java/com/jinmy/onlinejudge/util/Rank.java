package com.jinmy.onlinejudge.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jinmy.onlinejudge.entity.Contest;
import com.jinmy.onlinejudge.entity.ContestProblem;
import com.jinmy.onlinejudge.entity.Solution;
import lombok.Data;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
public class Rank {
    @JsonIgnore
    Contest contest;
    @JsonIgnore
    List<ContestProblem> contestProblemList;
    @JsonIgnore
    Map<Long, Long> problemIdMap = new HashMap<>();// Pid :: temp Id
    Map<Long, Person> personMap = new HashMap<>();//User Id :: Person

    public Rank(Contest contest) {
        this.contest = contest;
        this.contestProblemList = this.contest.getProblems();
    }

    public void init(List<Solution> solutionList) {
        for (ContestProblem cp : contestProblemList) {
            problemIdMap.put(cp.getProblem().getId(), cp.getTempId());
        }
        for (Solution s : solutionList) {
            s.getProblem().setId(problemIdMap.get(s.getProblem().getId()));
            if (personMap.containsKey(s.getUser().getId())) {
                personMap.get(s.getUser().getId()).update(s);
            } else {
                personMap.put(s.getUser().getId(), new Person(contest));
                personMap.get(s.getUser().getId()).update(s);
            }
        }
    }
}

@Data
class Person {
    private Contest contest;
    private Long penalty;
    private Long ac;
    private Map<Long, RankProblem> map;//temp Id :: rankproblem

    Person(Contest contest) {
        this.penalty = 0L;
        this.ac = 0L;
        map = new HashMap<>();
        this.contest = contest;
    }

    void update(Solution s) {
        RankProblem rp = new RankProblem();
        rp.setId(s.getProblem().getId());
        if (map.containsKey(rp.getId())) {
            rp = map.get(rp.getId());
            if (rp.isAc()) return;//AC
            if (s.getResult().equals("Accepted")) {
                rp.setAc(true);
                rp.setDuration(Duration.between(contest.getStartTime(), s.getSubmitTime()));
                this.penalty += rp.getDuration().getSeconds();
                this.penalty += rp.getWa() * 20L;
                this.ac++;
            } else if (s.getResult().indexOf("Runn") != -1 || s.getResult().indexOf("Compile") != -1) {
                return;
            } else {
                rp.setWa(rp.getWa() + 1L);
            }
        }
    }
}

@Data
class RankProblem {
    private Long id;
    private boolean isAc;
    private Long wa;
    private Duration duration;

    public RankProblem() {
        id = 0L;
        wa = 0L;
        duration = Duration.ofSeconds(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RankProblem)) return false;
        RankProblem that = (RankProblem) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

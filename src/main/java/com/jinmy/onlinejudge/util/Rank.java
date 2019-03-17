package com.jinmy.onlinejudge.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jinmy.onlinejudge.entity.Contest;
import com.jinmy.onlinejudge.entity.ContestProblem;
import com.jinmy.onlinejudge.entity.Solution;
import com.jinmy.onlinejudge.entity.User;
import lombok.Data;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Rank {
    @JsonIgnore
    Contest contest;
    @JsonIgnore
    List<ContestProblem> contestProblemList;
    @JsonIgnore
    Map<Long, Long> problemIdMap = new HashMap<>();// Pid :: temp Id
    @JsonIgnore
    Map<Long, Person> personMap = new HashMap<>();//User Id :: Person
    List<Person> people;
    @JsonIgnore
    Map<Long, Long> durationMap = new HashMap<>();////temp id :: minimal duration seconds

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
            if (s.getResult().equals("Accepted")) {
                if (durationMap.containsKey(s.getProblem().getId())) {
                    durationMap.put(s.getProblem().getId(),
                            Math.min(Duration.between(contest.getStartTime(), s.getSubmitTime()).getSeconds(),
                                    durationMap.get(s.getProblem().getId())));
                } else {
                    durationMap.put(s.getProblem().getId(), Duration.between(contest.getStartTime(), s.getSubmitTime()).getSeconds());
                }
            }
        }
        people = new ArrayList<>();
        for (Map.Entry<Long, Person> entry : personMap.entrySet()) {
            entry.getValue().getResult();
            for (RankProblem rp : entry.getValue().problems) {
                if (rp.isAc()) {
                    if (rp.duration.getSeconds() == durationMap.get(rp.getPid())) {
                        rp.setFirstblood(true);
                    }
                }
            }
            people.add(entry.getValue());
        }
        people.sort((o1, o2) -> {
            if (o1.getAc() == o2.getAc())
                return (int) (o1.getPenalty() - o2.getPenalty());
            return (int) (-o1.getAc() + o2.getAc());
        });
    }
}

@Data
class Person {
    List<RankProblem> problems;
    @JsonIgnore
    private Contest contest;
    private Long penalty;
    private Long ac;
    private User user;
    @JsonIgnore
    private Map<Long, RankProblem> map;//temp Id :: rankproblem

    Person(Contest c) {
        problems = new ArrayList<>();
        this.penalty = 0L;
        this.ac = 0L;
        map = new HashMap<>();
        this.contest = c;
    }

    public Long getPenalty() {
        return penalty / 60;
    }

    void update(Solution s) {
        user = s.getUser();
        RankProblem rp = new RankProblem();
        rp.setPid(s.getProblem().getId());
        if (map.containsKey(rp.getPid())) {
            rp = map.get(rp.getPid());
        }
        if (rp.isAc()) return;//AC
        if (s.getResult().equals("Accepted")) {
            rp.setAc(true);
            rp.setDuration(Duration.between(contest.getStartTime(), s.getSubmitTime()));
            this.penalty += rp.duration.getSeconds();
            this.penalty += rp.getWa() * 20 * 60L;
            this.ac++;
        } else if (s.getResult().indexOf("Runn") != -1 || s.getResult().indexOf("Compi") != -1) {
            return;
        } else {
            rp.setWa(rp.getWa() + 1L);
        }
        map.put(rp.getPid(), rp);
    }

    void getResult() {
        for (Map.Entry<Long, RankProblem> entry : map.entrySet()) {
            problems.add(entry.getValue());
        }
        problems.sort((o1, o2) -> (int) (o1.getPid() - o2.getPid()));
    }
}

@Data
class RankProblem {
    private boolean firstblood;
    Duration duration;
    private boolean isAc;
    private Long wa;
    private Long pid;

    public RankProblem() {
        firstblood = false;
        pid = 0L;
        wa = 0L;
        isAc = false;
        duration = Duration.ofSeconds(0);
    }

    public Long getDuration() {
        return duration.toMinutes();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RankProblem)) return false;
        RankProblem that = (RankProblem) o;
        return pid.equals(that.pid);
    }
}
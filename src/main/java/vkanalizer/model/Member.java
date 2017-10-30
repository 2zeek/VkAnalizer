package vkanalizer.model;

import com.vk.api.sdk.objects.groups.UserXtrRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Member {

    private Integer id;
    private String firstName;
    private String lastName;

    public Member(Integer id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + "vk.com/id" + id + ")";
    }

    public static List<Member> parseToMember(List<UserXtrRole> list) {
        List<Member> membersList = new ArrayList<>();
        for (UserXtrRole user : list)
            membersList.add(new Member(user.getId(), user.getFirstName(), user.getLastName()));
        return membersList;
    }
}

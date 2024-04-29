//package com.devsplan.ketchup.auth.model;
//
//
//
//import com.devsplan.ketchup.member.entity.Member;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Optional;
//
//public class DetailsMember implements UserDetails {
//
//    private Member member;
//
//    public DetailsMember() {
//    }
//
//    public DetailsMember(Optional<Member> member) {
//        this.member = member.get();
//    }
//
//    public Member getMember() {
//        return member;
//    }
//
//    public void setMember(Member member) {
//        this.member = member;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        member.getPosition().getRoleList().forEach(role -> authorities.add(() -> role));
//        return authorities;
//    }
//
//
//    @Override
//    public String getPassword() {
//        return member.getMemberPW();
//    }
//
//    @Override
//    public String getUsername() {
//        return ""+member.getMemberNo();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}

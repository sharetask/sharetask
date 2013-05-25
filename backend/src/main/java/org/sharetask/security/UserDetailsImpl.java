package org.sharetask.security;

import java.util.Collection;

import lombok.Getter;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class UserDetailsImpl extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 1L;

	@Getter
	private final String salt;
	
	public UserDetailsImpl(final String username, final String password, final String salt, final boolean enabled,
			final boolean accountNonExpired, final boolean credentialsNonExpired, final boolean accountNonLocked,
			final Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.salt = salt;
	}

	public UserDetailsImpl(final String username, final String password, final String salt, final Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.salt = salt;
	}
}

CREATE OR REPLACE FUNCTION data.add_user(name text, id text) RETURNS bool AS $$
DECLARE 
	passwd_hash integer := null;
BEGIN
    SELECT password into passwd_hash FROM data._user WHERE user_id = name limit 1;
    IF (passwd_hash IS NOT NULL) THEN
        IF (passwd_hash = _password) THEN
			RETURN true;
		ELSE
			RETURN false;
		END IF;
    ELSE
        insert into data._user(user_id, telegram_id, password, enable_notification) values (name, id, _password, true);
        return true;
    end if;
END;
$$ LANGUAGE plpgsql;

create or replace function data.insert_list(id text, name text) returns integer as
$$
DECLARE
    _list_id integer;
BEGIN
    insert into data.list(list_description, owner_id) values (name, id) returning list_id into _list_id;
    return _list_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION data.get_user_lists(id text) RETURNS table(name text) AS
$$
BEGIN
    SELECT l.list_description FROM data.list l, data.share_list sl
            WHERE l.list_id = sl.list_id AND user_id = id;
END;
$$ LANGUAGE plpgsql;

create or replace FUNCTION data.share_list(_list_id int, _user_id text) returns int as
$$
BEGIN
    if exists(select * from data.share_list where _list_id = list_id and _user_id = user_id) then
        return false;
    else
        insert into data.share_list(list_id, user_id) VALUES (_list_id, _user_id);
        return true;
    end if;
end;
$$ LANGUAGE plpgsql;

package org.palladiosimulator.protocom.lang.java.util;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Set;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.AssemblyEventConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyInfrastructureConnector;
import org.palladiosimulator.pcm.core.composition.ComposedStructure;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.core.composition.DelegationConnector;
import org.palladiosimulator.pcm.core.entity.InterfaceProvidingEntity;
import org.palladiosimulator.pcm.repository.InfrastructureRequiredRole;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.RequiredRole;
import org.palladiosimulator.pcm.repository.Role;
import org.palladiosimulator.pcm.repository.SinkRole;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.Branch;
import org.palladiosimulator.pcm.usagemodel.BranchTransition;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.Loop;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;

/**
 * Templates for external (service) calls.
 * Converted from PCM's OCL statements.
 * 
 * @author Thomas Zolynski
 */
@SuppressWarnings("all")
public class PcmCalls {
  /**
   * Query System Calls.
   */
  public static Iterable<EntryLevelSystemCall> querySystemCalls(final ScenarioBehaviour scenBe) {
    Set<EntryLevelSystemCall> _xblockexpression = null;
    {
      final LinkedList<EntryLevelSystemCall> results = CollectionLiterals.<EntryLevelSystemCall>newLinkedList();
      EList<AbstractUserAction> _actions_ScenarioBehaviour = scenBe.getActions_ScenarioBehaviour();
      final Function1<AbstractUserAction, Boolean> _function = (AbstractUserAction it) -> {
        return Boolean.valueOf(EntryLevelSystemCall.class.isInstance(it));
      };
      Iterable<AbstractUserAction> _filter = IterableExtensions.<AbstractUserAction>filter(_actions_ScenarioBehaviour, _function);
      final Function1<AbstractUserAction, EntryLevelSystemCall> _function_1 = (AbstractUserAction it) -> {
        return ((EntryLevelSystemCall) it);
      };
      Iterable<EntryLevelSystemCall> _map = IterableExtensions.<AbstractUserAction, EntryLevelSystemCall>map(_filter, _function_1);
      Iterables.<EntryLevelSystemCall>addAll(results, _map);
      Iterable<EntryLevelSystemCall> _querySystemCallsInLoops = PcmCalls.querySystemCallsInLoops(scenBe);
      Iterables.<EntryLevelSystemCall>addAll(results, _querySystemCallsInLoops);
      Iterable<EntryLevelSystemCall> _querySystemCallsInBraches = PcmCalls.querySystemCallsInBraches(scenBe);
      Iterables.<EntryLevelSystemCall>addAll(results, _querySystemCallsInBraches);
      _xblockexpression = IterableExtensions.<EntryLevelSystemCall>toSet(results);
    }
    return _xblockexpression;
  }
  
  public static Iterable<EntryLevelSystemCall> querySystemCallsInLoops(final ScenarioBehaviour sb) {
    EList<AbstractUserAction> _actions_ScenarioBehaviour = sb.getActions_ScenarioBehaviour();
    final Function1<AbstractUserAction, Boolean> _function = (AbstractUserAction it) -> {
      return Boolean.valueOf(Loop.class.isInstance(it));
    };
    Iterable<AbstractUserAction> _filter = IterableExtensions.<AbstractUserAction>filter(_actions_ScenarioBehaviour, _function);
    final Function1<AbstractUserAction, Loop> _function_1 = (AbstractUserAction it) -> {
      return ((Loop) it);
    };
    Iterable<Loop> _map = IterableExtensions.<AbstractUserAction, Loop>map(_filter, _function_1);
    final Function1<Loop, Iterable<EntryLevelSystemCall>> _function_2 = (Loop it) -> {
      ScenarioBehaviour _bodyBehaviour_Loop = it.getBodyBehaviour_Loop();
      return PcmCalls.querySystemCalls(_bodyBehaviour_Loop);
    };
    Iterable<Iterable<EntryLevelSystemCall>> _map_1 = IterableExtensions.<Loop, Iterable<EntryLevelSystemCall>>map(_map, _function_2);
    return Iterables.<EntryLevelSystemCall>concat(_map_1);
  }
  
  public static Iterable<EntryLevelSystemCall> querySystemCallsInBraches(final ScenarioBehaviour scenBe) {
    EList<AbstractUserAction> _actions_ScenarioBehaviour = scenBe.getActions_ScenarioBehaviour();
    final Function1<AbstractUserAction, Boolean> _function = (AbstractUserAction it) -> {
      return Boolean.valueOf(Branch.class.isInstance(it));
    };
    Iterable<AbstractUserAction> _filter = IterableExtensions.<AbstractUserAction>filter(_actions_ScenarioBehaviour, _function);
    final Function1<AbstractUserAction, Branch> _function_1 = (AbstractUserAction it) -> {
      return ((Branch) it);
    };
    Iterable<Branch> _map = IterableExtensions.<AbstractUserAction, Branch>map(_filter, _function_1);
    final Function1<Branch, EList<BranchTransition>> _function_2 = (Branch it) -> {
      return it.getBranchTransitions_Branch();
    };
    Iterable<EList<BranchTransition>> _map_1 = IterableExtensions.<Branch, EList<BranchTransition>>map(_map, _function_2);
    Iterable<BranchTransition> _flatten = Iterables.<BranchTransition>concat(_map_1);
    final Function1<BranchTransition, Iterable<EntryLevelSystemCall>> _function_3 = (BranchTransition it) -> {
      ScenarioBehaviour _branchedBehaviour_BranchTransition = it.getBranchedBehaviour_BranchTransition();
      return PcmCalls.querySystemCalls(_branchedBehaviour_BranchTransition);
    };
    Iterable<Iterable<EntryLevelSystemCall>> _map_2 = IterableExtensions.<BranchTransition, Iterable<EntryLevelSystemCall>>map(_flatten, _function_3);
    return Iterables.<EntryLevelSystemCall>concat(_map_2);
  }
  
  public static Iterable<org.palladiosimulator.pcm.system.System> getSystemsFromCalls(final Iterable<EntryLevelSystemCall> calls) {
    final Function1<EntryLevelSystemCall, InterfaceProvidingEntity> _function = (EntryLevelSystemCall it) -> {
      OperationProvidedRole _providedRole_EntryLevelSystemCall = it.getProvidedRole_EntryLevelSystemCall();
      return _providedRole_EntryLevelSystemCall.getProvidingEntity_ProvidedRole();
    };
    Iterable<InterfaceProvidingEntity> _map = IterableExtensions.<EntryLevelSystemCall, InterfaceProvidingEntity>map(calls, _function);
    final Function1<InterfaceProvidingEntity, org.palladiosimulator.pcm.system.System> _function_1 = (InterfaceProvidingEntity it) -> {
      return ((org.palladiosimulator.pcm.system.System) it);
    };
    Iterable<org.palladiosimulator.pcm.system.System> _map_1 = IterableExtensions.<InterfaceProvidingEntity, org.palladiosimulator.pcm.system.System>map(_map, _function_1);
    return IterableExtensions.<org.palladiosimulator.pcm.system.System>toSet(_map_1);
  }
  
  /**
   * Query Ports.
   */
  public static CharSequence portQuery(final OperationRequiredRole role, final ComposedStructure s, final AssemblyContext ctx) {
    CharSequence _xifexpression = null;
    boolean _hasConnector = PcmCalls.hasConnector(s, ctx, role);
    if (_hasConnector) {
      CharSequence _xblockexpression = null;
      {
        Connector _connector = PcmCalls.getConnector(s, ctx, role);
        final AssemblyConnector connector = ((AssemblyConnector) _connector);
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("(");
        OperationInterface _requiredInterface__OperationRequiredRole = role.getRequiredInterface__OperationRequiredRole();
        String _fqn = JavaNames.fqn(_requiredInterface__OperationRequiredRole);
        _builder.append(_fqn, "");
        _builder.append(") my");
        AssemblyContext _providingAssemblyContext_AssemblyConnector = connector.getProvidingAssemblyContext_AssemblyConnector();
        String _javaName = JavaNames.javaName(_providingAssemblyContext_AssemblyConnector);
        _builder.append(_javaName, "");
        _xblockexpression = _builder;
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }
  
  /**
   * Connectors.
   */
  public static Connector getConnector(final ComposedStructure cs, final AssemblyContext context, final OperationRequiredRole role) {
    Iterable<Connector> _connectors = PcmCalls.connectors(cs);
    final Function1<Connector, Boolean> _function = (Connector con) -> {
      return Boolean.valueOf(PcmCalls.test(con, context, role));
    };
    Iterable<Connector> _filter = IterableExtensions.<Connector>filter(_connectors, _function);
    return ((Connector[])Conversions.unwrapArray(_filter, Connector.class))[0];
  }
  
  public static boolean hasConnector(final ComposedStructure cs, final AssemblyContext context, final OperationRequiredRole role) {
    return true;
  }
  
  public static Iterable<Connector> connectors(final ComposedStructure s) {
    EList<Connector> _connectors__ComposedStructure = s.getConnectors__ComposedStructure();
    final Function1<Connector, Boolean> _function = (Connector it) -> {
      boolean _isInstance = DelegationConnector.class.isInstance(it);
      return Boolean.valueOf((!_isInstance));
    };
    return IterableExtensions.<Connector>filter(_connectors__ComposedStructure, _function);
  }
  
  protected static boolean _test(final AssemblyConnector c, final AssemblyContext ctx, final RequiredRole r) {
    boolean _and = false;
    OperationRequiredRole _requiredRole_AssemblyConnector = c.getRequiredRole_AssemblyConnector();
    String _id = _requiredRole_AssemblyConnector.getId();
    String _id_1 = r.getId();
    boolean _equals = Objects.equal(_id, _id_1);
    if (!_equals) {
      _and = false;
    } else {
      AssemblyContext _requiringAssemblyContext_AssemblyConnector = c.getRequiringAssemblyContext_AssemblyConnector();
      String _id_2 = _requiringAssemblyContext_AssemblyConnector.getId();
      String _id_3 = ctx.getId();
      boolean _equals_1 = Objects.equal(_id_2, _id_3);
      _and = _equals_1;
    }
    return _and;
  }
  
  protected static boolean _test(final AssemblyInfrastructureConnector c, final AssemblyContext ctx, final InfrastructureRequiredRole r) {
    boolean _and = false;
    InfrastructureRequiredRole _requiredRole__AssemblyInfrastructureConnector = c.getRequiredRole__AssemblyInfrastructureConnector();
    String _id = _requiredRole__AssemblyInfrastructureConnector.getId();
    String _id_1 = r.getId();
    boolean _equals = Objects.equal(_id, _id_1);
    if (!_equals) {
      _and = false;
    } else {
      AssemblyContext _requiringAssemblyContext__AssemblyInfrastructureConnector = c.getRequiringAssemblyContext__AssemblyInfrastructureConnector();
      String _id_2 = _requiringAssemblyContext__AssemblyInfrastructureConnector.getId();
      String _id_3 = ctx.getId();
      boolean _equals_1 = Objects.equal(_id_2, _id_3);
      _and = _equals_1;
    }
    return _and;
  }
  
  protected static boolean _test(final AssemblyInfrastructureConnector c, final AssemblyContext ctx, final OperationRequiredRole r) {
    boolean _and = false;
    InfrastructureRequiredRole _requiredRole__AssemblyInfrastructureConnector = c.getRequiredRole__AssemblyInfrastructureConnector();
    String _id = _requiredRole__AssemblyInfrastructureConnector.getId();
    String _id_1 = r.getId();
    boolean _equals = Objects.equal(_id, _id_1);
    if (!_equals) {
      _and = false;
    } else {
      AssemblyContext _requiringAssemblyContext__AssemblyInfrastructureConnector = c.getRequiringAssemblyContext__AssemblyInfrastructureConnector();
      String _id_2 = _requiringAssemblyContext__AssemblyInfrastructureConnector.getId();
      String _id_3 = ctx.getId();
      boolean _equals_1 = Objects.equal(_id_2, _id_3);
      _and = _equals_1;
    }
    return _and;
  }
  
  protected static boolean _test(final AssemblyEventConnector c, final AssemblyContext ctx, final SinkRole r) {
    boolean _and = false;
    SinkRole _sinkRole__AssemblyEventConnector = c.getSinkRole__AssemblyEventConnector();
    String _id = _sinkRole__AssemblyEventConnector.getId();
    boolean _equals = Objects.equal(_id, r);
    if (!_equals) {
      _and = false;
    } else {
      AssemblyContext _sourceAssemblyContext__AssemblyEventConnector = c.getSourceAssemblyContext__AssemblyEventConnector();
      String _id_1 = _sourceAssemblyContext__AssemblyEventConnector.getId();
      String _id_2 = ctx.getId();
      boolean _equals_1 = Objects.equal(_id_1, _id_2);
      _and = _equals_1;
    }
    return _and;
  }
  
  protected static boolean _test(final DelegationConnector c, final AssemblyContext ctx, final SinkRole r) {
    return false;
  }
  
  public static boolean test(final Connector c, final AssemblyContext ctx, final Role r) {
    if (c instanceof AssemblyEventConnector
         && r instanceof SinkRole) {
      return _test((AssemblyEventConnector)c, ctx, (SinkRole)r);
    } else if (c instanceof AssemblyInfrastructureConnector
         && r instanceof InfrastructureRequiredRole) {
      return _test((AssemblyInfrastructureConnector)c, ctx, (InfrastructureRequiredRole)r);
    } else if (c instanceof AssemblyInfrastructureConnector
         && r instanceof OperationRequiredRole) {
      return _test((AssemblyInfrastructureConnector)c, ctx, (OperationRequiredRole)r);
    } else if (c instanceof DelegationConnector
         && r instanceof SinkRole) {
      return _test((DelegationConnector)c, ctx, (SinkRole)r);
    } else if (c instanceof AssemblyConnector
         && r instanceof RequiredRole) {
      return _test((AssemblyConnector)c, ctx, (RequiredRole)r);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(c, ctx, r).toString());
    }
  }
}

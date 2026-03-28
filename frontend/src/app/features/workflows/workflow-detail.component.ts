import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { HeaderComponent } from '../../shared/components/header/header.component';
import { WorkflowService } from '../../core/services/workflow.service';
import { AuthService } from '../../core/services/auth.service';
import { WorkflowDefinition } from '../../core/models/workflow.model';

@Component({
  selector: 'app-workflow-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, HeaderComponent],
  template: `
    <app-header [title]="workflow()?.name ?? 'Workflow'">
      <a routerLink="/workflows" class="btn btn-secondary">Back</a>
    </app-header>
    <div class="page-content">
      @if (workflow(); as wf) {
        <div class="workflow-info">
          <span [class]="wf.active ? 'badge badge-success' : 'badge badge-muted'">
            {{ wf.active ? 'Active' : 'Inactive' }}
          </span>
          <span class="version">v{{ wf.version }}</span>
          @if (wf.description) {
            <p>{{ wf.description }}</p>
          }
        </div>

        <div class="state-machine">
          <h3>State Machine</h3>
          <div class="states">
            @for (state of wf.stateMachine.states; track state) {
              <span
                [class]="'state-badge ' +
                  (state === wf.stateMachine.initial ? 'initial' : '') +
                  (wf.stateMachine.terminal.includes(state) ? 'terminal' : '')"
              >
                {{ state }}
                @if (state === wf.stateMachine.initial) {
                  <small>(initial)</small>
                }
                @if (wf.stateMachine.terminal.includes(state)) {
                  <small>(terminal)</small>
                }
              </span>
            }
          </div>
          <div class="transitions">
            <h4>Transitions</h4>
            @for (t of wf.stateMachine.transitions; track t.name) {
              <div class="transition-item">
                <strong>{{ t.name }}</strong>: {{ t.from }} &rarr; {{ t.to }}
              </div>
            }
          </div>
        </div>
      }
    </div>
  `,
  styles: [
    `
      .page-content {
        padding: 24px 32px;
        max-width: 800px;
      }
      .workflow-info {
        display: flex;
        align-items: center;
        gap: 12px;
        margin-bottom: 24px;
      }
      .workflow-info p {
        color: #9ca3af;
        margin: 0;
      }
      .version {
        color: #6b7280;
        font-size: 13px;
      }
      .state-machine {
        margin-top: 24px;
      }
      .state-machine h3,
      .state-machine h4 {
        color: #e2e8f0;
        margin: 0 0 12px;
      }
      .states {
        display: flex;
        flex-wrap: wrap;
        gap: 8px;
        margin-bottom: 20px;
      }
      .state-badge {
        padding: 6px 12px;
        background: #22262e;
        border: 1px solid #374151;
        border-radius: 6px;
        color: #c4c9d4;
        font-size: 13px;
      }
      .state-badge.initial {
        border-color: #2563eb;
      }
      .state-badge.terminal {
        border-color: #059669;
      }
      .state-badge small {
        color: #6b7280;
        font-size: 10px;
        margin-left: 4px;
      }
      .transition-item {
        padding: 8px 12px;
        background: #1a1d23;
        border: 1px solid #2d3139;
        border-radius: 6px;
        color: #c4c9d4;
        font-size: 13px;
        margin-bottom: 8px;
      }
      .transition-item strong {
        color: #e2e8f0;
      }
    `,
  ],
})
export class WorkflowDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private workflowService = inject(WorkflowService);
  auth = inject(AuthService);

  workflow = signal<WorkflowDefinition | null>(null);

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.workflowService.getDefinition(id).subscribe((wf) => this.workflow.set(wf));
    }
  }
}
